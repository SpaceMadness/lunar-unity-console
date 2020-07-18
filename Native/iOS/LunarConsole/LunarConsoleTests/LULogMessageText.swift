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
        let tags = [LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(5, 2))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This <b>is</b> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags2() throws {
        let tags = [LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(0, 7))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("<b>This is</b> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testBoldTags3() throws {
        let tags = [LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(8, 5))];
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
    
    func testItalicTags1() throws {
        let tags = [LURichTextStyleTag(style: LURichTextStyleItalic, range: NSMakeRange(5, 2))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This <i>is</i> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testItalicTags2() throws {
        let tags = [LURichTextStyleTag(style: LURichTextStyleItalic, range: NSMakeRange(0, 7))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("<i>This is</i> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testItalicTags3() throws {
        let tags = [LURichTextStyleTag(style: LURichTextStyleItalic, range: NSMakeRange(8, 5))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This is <i>text.</i>");
        XCTAssertEqual(expected, actual);
    }
    
    func testItalicTags4() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is <i></i>text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testItalicTags5() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is <i><i></i></i>text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testColorTags1() throws {
        let tags = [LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(5, 2))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This <color=red>is</color> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testColorTags2() throws {
        let tags = [LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(0, 7))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("<color=red>This is</color> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testColorTags3() throws {
        let tags = [LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(8, 5))];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This is <color=red>text.</color>");
        XCTAssertEqual(expected, actual);
    }
    
    func testColorTags4() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is <color=red></color>text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testColorTags5() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This is <color=red><color=red></color></color>text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testMultipleTags1() throws {
        let tags = [
            LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(5, 2)),
            LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(5, 2))
        ];
        let expected = LULogMessage(text: "This is text.", tags: tags);
        let actual = LULogMessage.fromRichText("This <color=red><b>is</b></color> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testMultipleTags2() throws {
        let tags = [
            LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(12, 4)),
            LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(8, 19))
        ];
        let expected = LULogMessage(text: "This is red bold attributed text.", tags: tags);
        let actual = LULogMessage.fromRichText("This is <color=red>red <b>bold</b> attributed</color> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testMultipleTags3() throws {
        let tags = [
            LURichTextStyleTag(style: LURichTextStyleBoldItalic, range: NSMakeRange(17, 3)),
            LURichTextStyleTag(style: LURichTextStyleBold, range: NSMakeRange(12, 11)),
            LURichTextColorTag(color: LUUIColorFromRGB(0xff0000ff), range: NSMakeRange(8, 26))
        ];
        let expected = LULogMessage(text: "This is red bold italic attributed text.", tags: tags);
        let actual = LULogMessage.fromRichText("This is <color=red>red <b>bold <i>ita</i>lic</b> attributed</color> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testMalformedTags1() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This <b>is text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testMalformedTags2() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This <b>is<b> text.");
        XCTAssertEqual(expected, actual);
    }
    
    func testMalformedTags3() throws {
        let expected = LULogMessage(text: "This is text.", tags: nil);
        let actual = LULogMessage.fromRichText("This <b>is</i> text.");
        XCTAssertEqual(expected, actual);
    }
}
