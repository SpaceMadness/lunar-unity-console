Before('@automation') do
  dir_project = resolve_path File.expand_path('TestProject')
  file_plugin = resolve_path File.expand_path(Dir['temp/packages/*.unitypackage'].first)
  @project = TestProject.new dir_project
  @project.clean
  @project.import_package file_plugin
end

Given(/^Plugin is (NOT )?enabled$/) do |not_flag|
  enabled = not_flag.nil?
  method_name = enabled ? 'EnablePlugin' : 'DisablePlugin'
  unity_bin = resolve_path '/Applications/Unity/Unity.app/Contents/MacOS/Unity'

  cmd = %("#{unity_bin}" -quit -batchmode)
  cmd << " -executeMethod LunarConsolePluginInternal.Installer.#{method_name}"
  cmd << %( -projectPath "#{@project.dir_project}")

  exec_shell cmd, "Can't #{enabled ? 'enable' : 'disable'} plugin"
end

When(/^I export ([\w]+) application$/) do |platform|
  puts @project.export_app platform
end

Then(/^([\w]+) application should (NOT )?contain plugin files$/) do |platform, not_flag|
  contains = not_flag.nil?

  cmp = @project.compare_build_and_plugin_files platform
  if contains
    cmp.should == :all_files
  else
    cmp.should == :no_files
  end
end

And(/^(.*?) application should can be built$/) do |platform|
  @project.build_app platform
end