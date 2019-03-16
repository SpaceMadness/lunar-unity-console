//
//  LUPluginSettings.swift
//  LunarConsoleTests
//
//  Created by Alex Lementuev on 3/15/19.
//  Copyright © 2019 Space Madness. All rights reserved.
//

import XCTest

class LUPluginSettingsTest: TestCase {
	func testParseJson() {
		let jsonDict = readJsonFile(name: "editor-settings");
		let actual = LUPluginSettings(dictionary: jsonDict as! [AnyHashable : Any])
		
		// this looks ugly
		let expected = LUPluginSettings();
		let exceptionWarning = LUExceptionWarningSettings();
		exceptionWarning.displayMode = LUDisplayModeAll;
		expected.exceptionWarning = exceptionWarning;

		let logOverlayColors = LULogColors();
		logOverlayColors.exception = createOverlayColor(createColor(10, 11, 12, 13), createColor(14, 15, 16, 17));
		logOverlayColors.error = createOverlayColor(createColor(20, 21, 22, 23), createColor(24, 25, 26, 27));
		logOverlayColors.warning = createOverlayColor(createColor(30, 31, 32, 33), createColor(34, 35, 36, 37));
		logOverlayColors.debug = createOverlayColor(createColor(40, 41, 42, 43), createColor(44, 45, 46, 47));
		let logOverlay = LULogOverlaySettings();
		logOverlay.isEnabled = false;
		logOverlay.maxVisibleLines = 3;
		logOverlay.timeout = 1.0;
		logOverlay.colors = logOverlayColors;
		expected.logOverlay = logOverlay;

		expected.capacity = 4096;
		expected.trim = 512;
		expected.gesture = LUConsoleGestureSwipeDown;
		expected.removeRichTextTags = false;
		expected.sortActions = true;
		expected.sortVariables = false;
		expected.emails = [
			"a.lementuev@gmail.com",
			"lunar.plugin@gmail.com"
		];

		XCTAssertEqual(expected, actual);
	}
	
	private func readJsonFile(name: String) -> NSDictionary? {
		if let url = Bundle(for: type(of: self)).url(forResource: name, withExtension: "json") {
			if let jsonData = try? Data(contentsOf: url) {
				return try! JSONSerialization.jsonObject(with: jsonData, options: JSONSerialization.ReadingOptions.mutableContainers) as? NSDictionary
			}
		}
		
		XCTFail("Unable to read json file '\(name)'")
		return nil
	}
	
	private func createOverlayColor(_ foreground: LUColor , _ background: LUColor) -> LULogEntryColors {
		let colors = LULogEntryColors();
		colors.foreground = foreground;
		colors.background = background;
		return colors;
	}
	
	private func createColor(_ r: UInt8, _ g: UInt8, _ b: UInt8, _ a: UInt8) -> LUColor {
		let color = LUColor()
		color.r = r
		color.g = g
		color.b = b
		color.a = a
		return color
	}
}
