# Run: ruby Builder/rakefile_test.rb   (no Unity/gems needed; colorize/cucumber are stubbed)
require 'minitest/autorun'
require 'tmpdir'
require 'rake'

stubs = Dir.mktmpdir
File.write "#{stubs}/colorize.rb", "class String; def colorize(*) self; end; end"
Dir.mkdir "#{stubs}/cucumber"
Dir.mkdir "#{stubs}/cucumber/rake"
File.write "#{stubs}/cucumber/rake/task.rb", "module Cucumber; module Rake; class Task; def initialize; yield Struct.new(:cucumber_opts).new; end; end; end; end"
$LOAD_PATH.unshift stubs

Dir.chdir(__dir__) { load "#{__dir__}/Rakefile" }

class RakefileTest < Minitest::Test
  IOS_SRC = File.expand_path('../Native/iOS/LunarConsole/LunarConsole', __dir__)

  def ios(config)
    Object.new.send :list_ios_files, IOS_SRC, :configuration => config, :use_relative_path => true
  end

  def test_free_has_no_full_sources
    files = ios 'Free'
    assert files.any? { |f| f.start_with? 'Free/' }
    refute files.any? { |f| f.start_with? 'Full/' }
  end

  def test_full_has_no_free_sources
    files = ios 'Full'
    assert files.any? { |f| f.start_with? 'Full/' }
    refute files.any? { |f| f.start_with? 'Free/' }
  end

  def test_missing_configuration_fails
    assert_raises(Exception) { ios nil }
  end

  def test_export_unity_packages_builds_each_edition
    Rake::Task.tasks.each { |t| t.reenable }
    seen = []
    # stub the Unity-dependent parts, keep the real dependency graph
    %w(init clean build_native_android list_package_files).each { |n| Rake::Task["lunar:#{n}"].clear_actions }
    Rake::Task['lunar:build_native_ios'].clear_actions
    Rake::Task['lunar:build_native_ios'].enhance { seen << [:ios, $package_config] }
    Rake::Task['lunar:export_package'].clear_actions
    Rake::Task['lunar:export_package'].enhance { seen << [:export, $package_config] }

    Rake::Task['lunar:export_unity_packages'].invoke

    assert_equal [[:ios, 'Free'], [:export, 'Free'], [:ios, 'Full'], [:export, 'Full']], seen
  end
end
