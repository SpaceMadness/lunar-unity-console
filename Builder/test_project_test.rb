# Run: ruby Builder/test_project_test.rb   (no Unity/gems needed)
require 'minitest/autorun'

# test_project.rb pulls in colorize/zip via common.rb; stub them (same approach as rakefile_test.rb).
require 'tmpdir'
stubs = Dir.mktmpdir
File.write "#{stubs}/colorize.rb", "class String; def colorize(*) self; end; end"
File.write "#{stubs}/zip.rb", "module Zip; end"
$LOAD_PATH.unshift stubs
require_relative 'features/support/test_project'

class TestProjectTest < Minitest::Test
  PBXPROJ = <<-PBX
    AAA /* x.m */ = {isa = PBXFileReference; lastKnownFileType = sourcecode.c.objc; name = lunar_unity_native_interface.m; path = LunarConsole/Common/Console/Plugin/lunar_unity_native_interface.m; sourceTree = SOURCE_ROOT; };
    BBB /* x.m */ = {isa = PBXFileReference; lastKnownFileType = sourcecode.c.objc; name = x.m; path = "../../Assets/LunarConsole/Editor/iOS/Common/x.m"; sourceTree = SOURCE_ROOT; };
  PBX

  def test_lists_only_build_folder_plugin_refs
    files = TestProject.allocate.parse_xcodeproj_plugin_files PBXPROJ, 'LunarConsole'
    assert_equal ['Common/Console/Plugin/lunar_unity_native_interface.m'], files
  end
end
