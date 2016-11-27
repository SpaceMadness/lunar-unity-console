//
//  AlertView.swift
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/27/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import UIKit

typealias TextInputViewCompletion = (_ text: String) -> Void

class TextInputView: NSObject, UIAlertViewDelegate {
    
    private var title: String
    private var message: String
    private var cancelButton: String
    private var completion: TextInputViewCompletion?

    init(title: String, message: String, cancelButton: String, completion: TextInputViewCompletion?) {
        self.title = title
        self.message = message
        self.cancelButton = cancelButton
        self.completion = completion
    }
    
    func show() {
        let alertView = UIAlertView(title: title, message: message, delegate: self, cancelButtonTitle: cancelButton, otherButtonTitles: "Cancel")
        alertView.alertViewStyle = .plainTextInput
        alertView.show()
    }
    
    func alertView(_ alertView: UIAlertView, clickedButtonAt buttonIndex: Int) {
        if buttonIndex == 0 {
            if let completion = completion {
                completion(alertView.textField(at: 0)!.text!)
            }
        }
    }
}
