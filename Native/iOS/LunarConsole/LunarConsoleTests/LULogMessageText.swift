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
        let expected = LULogMessage(text: nil, tags: nil);
        let actual = LULogMessage.fromRichText(nil);
        XCTAssertEqual(expected, actual);
    }
    
    func testEmptyString() throws {
        let expected = LULogMessage(text: "", tags: nil);
        let actual = LULogMessage.fromRichText("");
        XCTAssertEqual(expected, actual);
    }
    
    func testNoRichTags() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags1() throws {
        let tags = [LURichTextTag(type: LURichTextTagTypeBold, attribute: nil, range: NSMakeRange(5, 2))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This <b>is</b> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags2() throws {
        let tags = [LURichTextTag(type: LURichTextTagTypeBold, attribute: nil, range: NSMakeRange(0, 7))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("<b>This is</b> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags3() throws {
        let tags = [LURichTextTag(type: LURichTextTagTypeBold, attribute: nil, range: NSMakeRange(8, 5))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This is <b>text.</b>");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags4() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is <b></b>text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags5() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is <b><b></b></b>text.");
        XCTAssertEqual(expected, actual);
    }
}
