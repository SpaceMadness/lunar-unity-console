require_relative '../../common'
require_relative '../../unity_project'
require_relative '../../android_archive'

class TestProject

  def initialize(path)
    @unity_project = UnityProject.new path, $bin_unity_export
  end

  def clean
    Dir.chdir @unity_project.dir_project do
      exec_shell 'git checkout -- .', "Can't git reset"
      exec_shell 'git clean -x -f -d', "Can't git clean"
    end
  end

  def import_package(path)
    @unity_project.import_package path
  end

  def export_app(platform)

    if platform == 'iOS'
      @unity_project.exec_unity_method 'LunarConsoleEditorInternal.AppExporter.SwitchToIOSBuildTarget'
      @unity_project.exec_unity_method 'LunarConsoleEditorInternal.AppExporter.PerformIOSBuild'
    elsif platform == 'Android'
      @unity_project.exec_unity_method 'LunarConsoleEditorInternal.AppExporter.PerformAndroidBuild'
    else
      raise ArgumentError.new("Unexpected platform: #{platform}")
    end

    return resolve_path "#{@unity_project.dir_project}/Build/#{platform}"

  end

  def compare_build_and_plugin_files(platform)
    if platform == 'iOS'
      return compare_ios_build_and_plugin_files
    elsif platform == 'Android'
      return compare_android_build_and_plugin_files
    else
      raise ArgumentError.new("Unexpected platform: #{platform}")
    end
  end

  def compare_ios_build_and_plugin_files
    dir_ios_plugin = 'Assets/LunarConsole/Editor/iOS'

    file_xcodeproj = resolve_path "#{@unity_project.dir_project}/Build/iOS/Unity-iPhone.xcodeproj/project.pbxproj"
    xcodeproj_files = list_xcodeproj_plugin_files file_xcodeproj, dir_ios_plugin
    return :no_files if xcodeproj_files.empty?

    plugin_files = list_ios_plugin_files resolve_path "#{@unity_project.dir_project}/#{dir_ios_plugin}"
    return :all_files if xcodeproj_files == plugin_files

    return :partial_files
  end

  def compare_android_build_and_plugin_files

    # list classes from apk
    file_apk = resolve_path Dir["#{@unity_project.dir_project}/Build/Android/*.apk"].first
    apk = AndroidArchive.new file_apk
    apk_classes = apk.list_classes &-> (name) {
      return name.start_with?('spacemadness/')
    }

    # list resource from apk
    apk_resources = apk.list_resources &-> (name) {
      return name.include? 'lunar_console_'
    }

    # list classes from jars included in plugin
    file_aar = resolve_path "#{@unity_project.dir_project}/Assets/LunarConsole/Editor/Android/lunar-console.aar"
    aar = AndroidArchive.new file_aar
    plugin_classes = aar.list_classes &-> (name) {
      return name.start_with?('spacemadness/')
    }

    # list resources included in plugin
    plugin_resources = aar.list_resources &-> (name) {
      return name.include? 'lunar_console_'
    }

    # check classes
    no_classes = true
    apk_classes.each do |cls|
      if plugin_classes.include? cls
        plugin_classes.delete cls
        no_classes = false
      end
    end

    # check resources
    no_resources = true
    apk_resources.each do |res|
      if plugin_resources.include? res
        plugin_resources.delete res
        no_resources = false
      end
    end

    return :no_files if no_classes && no_resources
    return :all_files if plugin_classes.empty? && plugin_resources.empty?

    return :partial_files

  end

  def list_android_plugin_classes(dir_libs)
    classes = []
    Dir["#{dir_libs}/*.jar"].each do |file_jar|
      classes.push *(list_jar_classes file_jar)
    end
    return classes
  end

  def list_android_plugin_resources(dir_res)
    resources = []
    Dir.chdir dir_res do
      Dir['**/*'].each do |file|
        next if File.directory? file
        next if File.extname(file) == '.meta'

        resources.push "res/#{file}"
      end
    end
    return resources
  end

  def list_jar_classes(file_jar)
    result = exec_shell %(jar -tf "#{file_jar}"), "Can't list jar content: #{file_jar}"
    lines = result.split "\n"

    classes = []
    lines.each {|line|
      next unless line.end_with? '.class'

      classes.push line[0..-7]
    }

    return classes
  end

  def build_app(platform)
    if platform == 'iOS'
      build_ios_app
    elsif platform == 'Android'
      raise ArgumentError.new("Unexpected platform: #{platform}")
    else
      raise ArgumentError.new("Unexpected platform: #{platform}")
    end
  end

  def list_ios_files_ex(path, list)
    Dir[path].each { |file|
      if File.directory? file
        list_ios_files_ex "#{file}/*", list
      else
        next if File.extname(file) == '.meta'
        next if File.extname(file) == '.projmods'
        list.push file
      end
    }
  end

  def list_ios_plugin_files(path)
    files = []
    Dir.chdir path do
      list_ios_files_ex '*', files
    end

    files.sort!
  end

  def list_xcodeproj_plugin_files(file_proj, subpath)
    text = File.read file_proj
    regex = Regexp.new(%({isa\s+=\s+PBXFileReference;\s+lastKnownFileType\s+=\s+.*?;\s+name\s+=\s+.*?;\s+path =\s+"?../../#{subpath}/(.*?)"?;\s+sourceTree\s+=\s+SOURCE_ROOT;\s+};))
    actual_files = []
    text.scan(regex).each { |capture|
      actual_files.push capture.first
    }
    actual_files.sort!
  end

  def build_ios_app
    file_xcodeproj = resolve_path "#{@unity_project.dir_project}/Build/iOS/Unity-iPhone.xcodeproj"
    exec_shell %(xcodebuild -project "#{file_xcodeproj}" -target Unity-iPhone -configuration Debug), "Can't build iOS app"
  end

  def dir_project
    @unity_project.dir_project
  end

end