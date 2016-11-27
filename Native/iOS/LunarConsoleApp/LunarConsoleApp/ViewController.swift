//
//  ViewController.swift
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/25/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import UIKit

class ViewController: LUViewController {
    
    private let kConsoleCapacity: UInt  = 4096
    private let kConsoleTrimCount: UInt = 512
    
    private(set) var plugin: LUConsolePlugin!
    private var index: Int = 0
    private var logEntries: [FakeLogEntry]!
    
    static var pluginInstance: LUConsolePlugin?
    
    @IBOutlet weak var messageText: UITextField!
    @IBOutlet weak var capacityText: UITextField!
    @IBOutlet weak var trimText: UITextField!
    
    deinit {
        ViewController.pluginInstance = nil
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        plugin = LUConsolePlugin(targetName: "LunarConsole", methodName: "OnNativeMessage", version: "0.0.0b", capacity: kConsoleCapacity, trimCount: kConsoleTrimCount, gestureName: "SwipeDown")
        
        capacityText.text = "\(kConsoleCapacity)"
        trimText.text = "\(kConsoleTrimCount)"
        
        ViewController.pluginInstance = plugin;
    }
    
    // MARK: - Actions
    
    @IBAction func onToggleLogger(sender: AnyObject) {
        showConsoleController()
        
        index = 0;
        logEntries = loadLogEntries()
        
        logNextMessage()
    }
    
    @IBAction func onShowController(sender: AnyObject) {
        showConsoleController()
    }
    
    @IBAction func onShowOverlay(sender: AnyObject) {
        showOverlay()
    }
    
    @IBAction func onShowAlert(sender: AnyObject) {
        showAlert()
    }
    
    @IBAction func onLogDebug(sender: AnyObject) {
        log(logType: LUConsoleLogTypeLog)
    }
    
    @IBAction func onLogWarning(sender: AnyObject) {
        log(logType: LUConsoleLogTypeWarning)
    }
    
    @IBAction func onLogError(sender: AnyObject) {
        log(logType: LUConsoleLogTypeError)
    }
    
    @IBAction func onSetCapacity(sender: AnyObject) {
        if let capacity = Int(capacityText.text!) {
            plugin.capacity = capacity
        }
    }
    
    @IBAction func onSetTrim(sender: AnyObject) {
        if let trim = Int(trimText.text!) {
            plugin.trim = trim;
        }
    }
    
    // MARK: - Log Entries
    
    private func loadLogEntries() -> [FakeLogEntry] {
        let URL = Bundle.main.url(forResource: "input", withExtension: "txt")!
        let data = try? Data(contentsOf: URL)
        let objects = try? JSONSerialization.jsonObject(with: data!) as! [Any]
        
        var entries = [FakeLogEntry]()
        for o in objects! {
            let object = o as! Dictionary<String, String>
            let level = object["level"]!
            let message = object["message"]!
            let stacktrace = object["stacktrace"]!
            
            var type = LUConsoleLogTypeLog
            if level == "ERROR" {
                type = LUConsoleLogTypeError
            }
            else if level == "WARNING" {
                type = LUConsoleLogTypeWarning
            }
            
            let entry = FakeLogEntry(type: type, message: message, stacktrace: stacktrace)
            entries.append(entry)
        }
        return entries;
    }

    
    // MARK: - Helpers
    
    private func showConsoleController() {
        plugin.showConsole()
    }
    
    private func showOverlay() {
        plugin.showActionOverlay()
    }
    
    private func showAlert() {
        let message = "Exception: Error!"
        let stackTrace = "Logger.LogError () (at Assets/Logger.cs:15)\n" +
        "UnityEngine.Events.InvokableCall.Invoke (System.Object[] args)\n " +
        "UnityEngine.Events.InvokableCallList.Invoke (System.Object[] parameters)\n " +
        "UnityEngine.Events.UnityEventBase.Invoke (System.Object[] parameters)\n " +
        "UnityEngine.Events.UnityEvent.Invoke ()\n " +
        "UnityEngine.UI.Button.Press () (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/UI/Core/Button.cs:35)\n " +
        "UnityEngine.UI.Button.OnPointerClick (UnityEngine.EventSystems.PointerEventData eventData) (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/UI/Core/Button.cs:44)\n " +
        "UnityEngine.EventSystems.ExecuteEvents.Execute (IPointerClickHandler handler, UnityEngine.EventSystems.BaseEventData eventData) (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/EventSystem/ExecuteEvents.cs:52)\n " +
        "UnityEngine.EventSystems.ExecuteEvents.Execute[IPointerClickHandler] (UnityEngine.GameObject target, UnityEngine.EventSystems.BaseEventData eventData, UnityEngine.EventSystems.EventFunction`1 functor) (at /Users/builduser/buildslave/unity/build/Extensions/guisystem/UnityEngine.UI/EventSystem/ExecuteEvents.cs:269)\n " +
        "UnityEngine.EventSystems.EventSystem:Update()"
        
        plugin.logMessage(message, stackTrace: stackTrace, type: LUConsoleLogTypeException)
    }

    private func logNextMessage() {
        let entry = logEntries[index]
        
        plugin.logMessage(entry.message, stackTrace: entry.stacktrace, type: entry.type)
        
        index = (index + 1) % logEntries.count;
        
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(200)) { [unowned self] in
            self.logNextMessage()
        }
    }
    
    private func log(logType: LUConsoleLogType) {
        self.log(message: messageText.text!, stackTrace: nil, type: logType)
    }
    
    private func log(message: String, stackTrace: String?, type: LUConsoleLogType) {
        plugin.logMessage(message, stackTrace: stackTrace, type: type)
    }
}

extension ViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return false
    }
}
