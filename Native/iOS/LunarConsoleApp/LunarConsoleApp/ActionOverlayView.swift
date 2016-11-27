//
//  ActionOverlayView.swift
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/26/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import UIKit

typealias ActionOverlayViewCallback = (_ text: String) -> Void

class ActionOverlayView: UIView {
    
    var callback: ActionOverlayViewCallback?
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.isOpaque = true
        self.backgroundColor = UIColor.white
        
        let button = UIButton(type: .contactAdd)
        button.addTarget(self, action: #selector(onButtonPress), for: .touchUpInside)
        addSubview(button)
        
        var buttonFrame = button.frame
        buttonFrame.origin = CGPoint(x: 0.5 * (frame.width - buttonFrame.width), y: 0.5 * (frame.height - buttonFrame.width))
        button.frame = buttonFrame
    }
    
    @objc private func onButtonPress(sender: UIButton) {
        let alertController = UIAlertController(title: "Command", message: "Enter command", preferredStyle: .alert)
        alertController.addTextField { (textField) in
            textField.placeholder = "Command"
        }
        
        alertController.addAction(UIAlertAction(title: "Run", style: .default, handler: { (action) in
            if let callback = self.callback {
                callback(alertController.textFields![0].text!)
            }
        }))
        
        alertController.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action) in
        }))
        
        UIApplication.shared.keyWindow?.rootViewController?.present(alertController, animated: true, completion: nil)
    }
}
