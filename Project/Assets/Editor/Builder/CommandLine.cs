//
//  CommandLine.cs
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

﻿using System;
using System.Collections.Generic;
using System.IO;

namespace LunarConsolePluginInternal
{
    static class CommandLine
    {
        private static readonly string kCustomArgsPrefix = "-customArgs?";

        public static IDictionary<string, string> Arguments
        {
            get
            {
                IDictionary<string, string> args = new Dictionary<string, string>();

                string argsToken = GetCustomArgToken();

                if (!string.IsNullOrEmpty(argsToken))
                {
                    string[] pairs = argsToken.Split('&');
                    foreach (string pair in pairs)
                    {
                        string[] tokens = pair.Split('=');
                        if (tokens.Length != 2)
                        {
                            throw new IOException("Unable to parse custom args: " + argsToken);
                        }

                        string key = tokens[0];
                        string value = tokens[1];
                        args[key] = value;
                    }
                }

                return args;
            }
        }

        private static string GetCustomArgToken()
        {
            string[] args = Environment.GetCommandLineArgs();
            foreach (string arg in args)
            {
                if (arg.StartsWith(kCustomArgsPrefix))
                {
                    return arg.Substring(kCustomArgsPrefix.Length);
                }
            }

            return null;
        }
    }
}

