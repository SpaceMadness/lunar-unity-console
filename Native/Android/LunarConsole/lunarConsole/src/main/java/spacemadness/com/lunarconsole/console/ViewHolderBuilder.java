package spacemadness.com.lunarconsole.console;

import android.view.ViewGroup;

public abstract class ViewHolderBuilder<T extends ConsoleEntry>
{
    public abstract ConsoleAdapter.ViewHolder<T> createViewHolder(ViewGroup parent);
}