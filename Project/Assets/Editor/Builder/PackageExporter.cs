//
//  PackageExporter.cs
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

﻿using UnityEngine;
using UnityEditor;

using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;

namespace LunarConsolePluginInternal
{
    static class PackageExporter
    {
        private static readonly string kArgumentAssetList = "assets";
        private static readonly string kArgumentOutputFile = "output";

        private static void ExportUnityPackage()
        {
            IDictionary<string, string> args = CommandLine.Arguments;

            string outputFile = GetCommandLineArg(args, kArgumentOutputFile);
            string[] assetList = GetCommandLineArray(args, kArgumentAssetList);

            DirectoryInfo outputDirectory = Directory.GetParent(outputFile);
            outputDirectory.Create();
            if (!outputDirectory.Exists)
            {
                throw new IOException("Can't create output directory: " + outputDirectory.FullName);
            }

            string projectDir = Directory.GetParent(Application.dataPath).FullName;

            Debug.Log("Checkings assets...");
            foreach (string asset in assetList)
            {
                string assetPath = Path.Combine(projectDir, asset);
                if (!File.Exists(assetPath) && !Directory.Exists(assetPath))
                {
                    throw new IOException("Asset does not exist: " + asset);
                }
            }

            Debug.Log("Exporting assets...");
            AssetDatabase.ExportPackage(assetList, outputFile);

            Debug.Log("Package written: " + outputFile);
        }

        private static string GetCommandLineArg(IDictionary<string, string> args, string key)
        {
            string value;
            if (!args.TryGetValue(key, out value))
            {
                throw new IOException("Missing command line argument: '" + key + "'");
            }

            if (string.IsNullOrEmpty(value))
            {
                throw new IOException("Command line argument is empty: '" + key + "'");
            }

            return value;
        }

        private static string[] GetCommandLineArray(IDictionary<string, string> args, string key, char delim = ',')
        {
            string value = GetCommandLineArg(args, key);
            return value.Split(delim);
        }
    }
}