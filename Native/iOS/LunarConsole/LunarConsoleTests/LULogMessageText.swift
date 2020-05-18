//
//  LULogMessageText.swift
//  LunarConsoleTests
//
//  Created by Alex Lementuev on 5/17/20.
//  Copyright © 2020 Space Madness. All rights reserved.
//

import XCTest

class LULogMessageText: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testNilString() throws {
        let expected = LULogMessage(text: nil, attributedText: nil);
        let actual = LULogMessage.fromRichText(nil);
        XCTAssertEqual(expected, actual);
    }
    
    func testEmptyString() throws {
        let expected = LULogMessage(text: "", attributedText: nil);
        let actual = LULogMessage.fromRichText("");
        XCTAssertEqual(expected, actual);
    }
    
    func testNoRichTags() throws {
        let expected = LULogMessage(text: "This is text.", attributedText: nil);
        let actual = LULogMessage.fromRichText("This is text.");
        XCTAssertEqual(expected, actual);
    }
}
