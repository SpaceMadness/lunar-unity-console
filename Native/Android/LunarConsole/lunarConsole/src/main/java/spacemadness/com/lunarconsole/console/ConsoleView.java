//
//  ConsoleView.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2016 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.console;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.ui.LogTypeButton;
import spacemadness.com.lunarconsole.ui.ToggleButton;
import spacemadness.com.lunarconsole.ui.ToggleImageButton;
import spacemadness.com.lunarconsole.utils.StackTrace;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.ThreadUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

import static android.widget.LinearLayout.LayoutParams.*;
import static spacemadness.com.lunarconsole.console.ConsoleLogType.*;
import static spacemadness.com.lunarconsole.debug.Tags.*;

public class ConsoleView extends LinearLayout implements
        Destroyable,
        LunarConsoleListener,
        LogTypeButton.OnStateChangeListener
{
    private final View rootView;

    private final Console console;
    private final ListView listView;
    private final ConsoleAdapter recyclerViewAdapter;

    private final LogTypeButton logButton;
    private final LogTypeButton warningButton;
    private final LogTypeButton errorButton;

    private final TextView overflowText;

    private ToggleImageButton scrollLockButton;

    private Listener listener;

    private boolean scrollLocked;
    private boolean softKeyboardVisible;

    public ConsoleView(Context context, final Console console)
    {
        super(context);

        if (console == null)
        {
            throw new NullPointerException("Console is null");
        }

        this.console = console;
        this.console.setConsoleListener(this);

        scrollLocked = ConsoleViewState.scrollLocked;

        // disable touches
        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true; // don't let touch events to pass through the view group
            }
        });

        // might not be the most efficient way but we'll keep it for now
        rootView = LayoutInflater.from(context).inflate(R.layout.lunar_layout_console, this, false);
        addView(rootView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

        // initialize adapter
        recyclerViewAdapter = new ConsoleAdapter(console);

        // this view would hold all the logs
        LinearLayout recyclerViewContainer = findExistingViewById(
                R.id.lunar_console_recycler_view_container);

        listView = new ListView(context);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setAdapter(recyclerViewAdapter);
        listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        listView.setScrollingCacheEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                final Context ctx = getContext();
                final ConsoleEntry entry = console.getEntry(position);

                // TODO: user color resource and animation
                view.setBackgroundColor(0xff000000);
                ThreadUtils.runOnUIThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            view.setBackgroundColor(entry.getBackgroundColor(ctx));
                        }
                        catch (Exception e)
                        {
                            Log.e(e, "Error while settings entry background color");
                        }
                    }
                }, 200);

                // TODO: refactor this code
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                LayoutInflater inflater = LayoutInflater.from(ctx);
                View contentView = inflater.inflate(R.layout.lunar_layout_log_details_dialog, null);
                ImageView imageView = (ImageView) contentView.findViewById(R.id.lunar_console_log_details_icon);
                TextView messageView = (TextView) contentView.findViewById(R.id.lunar_console_log_details_message);
                TextView stacktraceView = (TextView) contentView.findViewById(R.id.lunar_console_log_details_stacktrace);

                final String message = entry.message;
                final String stackTrace = entry.hasStackTrace() ?
                        StackTrace.optimize(entry.stackTrace) :
                        getResources().getString(R.string.lunar_console_log_details_dialog_no_stacktrace_warning);

                messageView.setText(message);
                stacktraceView.setText(stackTrace);
                imageView.setImageDrawable(entry.getIconDrawable(ctx));

                builder.setView(contentView);
                builder.setPositiveButton(R.string.lunar_console_log_details_dialog_button_copy_to_clipboard,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String text = message;
                                if (entry.hasStackTrace())
                                {
                                    text += "\n\n" + stackTrace;
                                }
                                copyToClipboard(text);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        listView.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (scrollLocked && event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    scrollLockButton.setOn(false);
                }

                return false; // don't consume the event
            }
        });

        recyclerViewContainer.addView(listView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));

        // setup filtering elements
        setupFilterTextEdit();
        logButton = findExistingViewById(R.id.lunar_console_log_button);
        warningButton = findExistingViewById(R.id.lunar_console_warning_button);
        errorButton = findExistingViewById(R.id.lunar_console_error_button);
        setupLogTypeButtons();

        setupOperationsButtons();

        // setup fake status bar
        setupFakeStatusBar();

        // setup overflow warning
        overflowText = findExistingViewById(R.id.lunar_console_text_overflow);

        reloadData();
    }

    @Override
    public void destroy()
    {
        Log.d(CONSOLE, "Destroy console");

        console.setConsoleListener(null);
        setListener(null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Filtering

    private void filterByText(String text)
    {
        boolean shouldReload = console.entries().setFilterByText(text);
        if (shouldReload)
        {
            reloadData();
        }
    }

    private void setFilterByLogTypeMask(int logTypeMask, boolean disabled)
    {
        boolean shouldReload = console.entries().setFilterByLogTypeMask(logTypeMask, disabled);
        if (shouldReload)
        {
            reloadData();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Back button

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {
        /*
        This part is a bit hacky: we want to hide console view on back button press and not finish
        the current activity. We intercept the event and don't let the system to handle it.
        Handling soft keyboard is a separate case: we set a boolean flag when user touches the filter
        input field and hide the keyboard manually if the flag is set. Without this hack the console
        view will be hidden when user dismisses the keyboard. The solution is quite ugly but I don't
        know a better way.
         */
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                if (softKeyboardVisible)
                {
                    hideSoftKeyboard();
                }
                else
                {
                    notifyClose();
                }
            }
            return true;
        }

        return super.dispatchKeyEventPreIme(event);
    }

    private void hideSoftKeyboard()
    {
        softKeyboardVisible = false;
        InputMethodManager manager = (InputMethodManager) getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    private void notifyClose()
    {
        softKeyboardVisible = false;

        if (listener != null)
        {
            listener.onClose(this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Data

    private void reloadData()
    {
        recyclerViewAdapter.notifyDataSetChanged();
        updateOverflowText();
    }

    private void clearConsole()
    {
        console.clear();
    }

    private boolean copyConsoleOutputToClipboard()
    {
        return copyToClipboard(console.getText());
    }

    private boolean sendConsoleOutputByEmail()
    {
        try
        {
            String packageName = getContext().getPackageName();
            String subject = StringUtils.TryFormat("'%s' console log", packageName);

            String outputText = console.getText();

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, outputText);

            if (intent.resolveActivity(getContext().getPackageManager()) != null)
            {
                getContext().startActivity(intent);
                return true;
            }

            UIUtils.showToast(getContext(), "Can't send email");
            return false;
        }
        catch (Exception e)
        {
            Log.e(e, "Error while trying to send console output by email");
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Scrolling

    private void toggleScrollLock()
    {
        scrollLocked = !scrollLocked;
        ConsoleViewState.scrollLocked = scrollLocked;

        scrollToBottom(console);
    }

    private void scrollToBottom(Console console)
    {
        if (scrollLocked)
        {
            int entryCount = console.getEntryCount();
            if (entryCount > 0)
            {
                listView.setSelection(entryCount - 1);
            }
        }
    }

    private void scrollToTop(Console console)
    {
        int entryCount = console.getEntryCount();
        if (entryCount > 0)
        {
            listView.setSelection(0);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UI elements

    private EditText setupFilterTextEdit()
    {
        // TODO: make a custom class
        EditText editText = findExistingViewById(R.id.lunar_console_text_edit_filter);
        String filterText = console.entries().getFilterText();
        if (!StringUtils.IsNullOrEmpty(filterText))
        {
            editText.setText(filterText);
            editText.setSelection(filterText.length());
        }

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filterByText(s.toString());
            }
        });
        editText.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                softKeyboardVisible = true;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    hideSoftKeyboard();
                    return true;
                }
                return false;
            }
        });

        return editText;
    }

    private void setupLogTypeButtons()
    {
        setupLogTypeButton(logButton, LOG);
        setupLogTypeButton(warningButton, WARNING);
        setupLogTypeButton(errorButton, ERROR);

        updateLogButtons();
    }

    private void setupLogTypeButton(LogTypeButton button, int logType)
    {
        button.setOn(console.entries().isFilterLogTypeEnabled(logType));
        button.setOnStateChangeListener(this);
    }

    private void setupOperationsButtons()
    {
        setOnClickListener(R.id.lunar_console_button_clear, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clearConsole();
            }
        });

        scrollLockButton = (ToggleImageButton) rootView.
                findViewById(R.id.lunar_console_button_lock);
        final Resources resources = getContext().getResources();
        scrollLockButton.setOnDrawable(resources.getDrawable(R.drawable.lunar_console_icon_button_lock));
        scrollLockButton.setOffDrawable(resources.getDrawable(R.drawable.lunar_console_icon_button_unlock));
        scrollLockButton.setOn(scrollLocked);
        scrollLockButton.setOnStateChangeListener(new ToggleImageButton.OnStateChangeListener()
        {
            @Override
            public void onStateChanged(ToggleImageButton button)
            {
                toggleScrollLock();
            }
        });

        setOnClickListener(R.id.lunar_console_button_copy, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                copyConsoleOutputToClipboard();
            }
        });

        setOnClickListener(R.id.lunar_console_button_email, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendConsoleOutputByEmail();
            }
        });

        setOnClickListener(R.id.lunar_console_button_close, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyClose();
            }
        });
    }

    private void setupFakeStatusBar()
    {
        String title = String.format(getResources().
                getString(R.string.lunar_console_title_fake_status_bar), ConsolePlugin.getVersion());

        TextView statusBar = findExistingViewById(R.id.lunar_console_fake_status_bar);
        statusBar.setText(title);
        statusBar.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                scrollLockButton.setOn(false);
                scrollToTop(console);
            }
        });
    }

    private void updateLogButtons()
    {
        final ConsoleEntryList entries = console.entries();
        logButton.setCount(entries.getLogCount());
        warningButton.setCount(entries.getWarningCount());
        errorButton.setCount(entries.getErrorCount());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LunarConsoleListener

    @Override
    public void onAddEntry(Console console, ConsoleEntry entry, boolean filtered)
    {
        if (filtered)
        {
            recyclerViewAdapter.notifyDataSetChanged();
            scrollToBottom(console);
        }

        updateLogButtons();
    }

    @Override
    public void onRemoveEntries(Console console, int start, int length)
    {
        recyclerViewAdapter.notifyDataSetChanged();
        scrollToBottom(console);
        updateLogButtons();
        updateOverflowText();
    }

    @Override
    public void onClearEntries(Console console)
    {
        reloadData();
        updateLogButtons();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Overflow

    private void updateOverflowText()
    {
        int trimmedCount = console.trimmedCount();
        if (trimmedCount > 0)
        {
            overflowText.setVisibility(View.VISIBLE);
            String text = getResources().getString(R.string.lunar_console_overflow_warning_text, trimmedCount);
            overflowText.setText(text);
        }
        else
        {
            overflowText.setVisibility(View.GONE);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LULogTypeButton.OnStateChangeListener

    @Override
    public void onStateChanged(ToggleButton button)
    {
        int mask = 0;
        if (button == logButton)
        {
            mask |= getMask(LOG);
        }
        else if (button == warningButton)
        {
            mask |= getMask(WARNING);
        }
        else if (button == errorButton)
        {
            mask |= getMask(EXCEPTION) |
                    getMask(ERROR) |
                    getMask(ASSERT);
        }

        setFilterByLogTypeMask(mask, !button.isOn());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    @SuppressWarnings("deprecation")
    private boolean copyToClipboard(String outputText)
    {
        try
        {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(outputText);

            UIUtils.showToast(getContext(), "Copied to clipboard");

            return true;
        }
        catch (Exception e)
        {
            Log.e(e, "Error to copy text to clipboard");
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public Listener getListener()
    {
        return listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helpers

    private <T extends View> T findExistingViewById(int id) throws ClassCastException
    {
        return findExistingViewById(rootView, id);
    }

    private <T extends View> T findExistingViewById(View parent, int id) throws ClassCastException
    {
        View view = parent.findViewById(id);
        if (view == null)
        {
            throw new IllegalArgumentException("View with id " + id + " not found");
        }

        return (T) view;
    }

    private void setOnClickListener(int viewId, View.OnClickListener listener)
    {
        View view = findExistingViewById(viewId);
        view.setOnClickListener(listener);
    }

    public interface Listener
    {
        void onClose(ConsoleView view);
    }
}
