//
//  FakeLogEntry.swift
//  LunarConsoleApp
//
//  Created by Alex Lementuev on 11/25/16.
//  Copyright © 2016 Space Madness. All rights reserved.
//

import CoreFoundation

class FakeLogEntry {
    
    private(set) var type: LUConsoleLogType
    private(set) var message: String
    private(set) var stacktrace: String?
    
    init(type: LUConsoleLogType, message: String, stacktrace: String?) {
        self.type = type
        self.message = message
        self.stacktrace = stacktrace
    }
}
