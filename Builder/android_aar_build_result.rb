require_relative 'common'

include Common

class AndroidArtifact
  attr_reader :name, :path, :version, :file_jar, :file_manifest, :dependencies, :dir_res

  def initialize(path, name, version)
    @path = resolve_path_e path
    @name = name
    @version = version

    @file_manifest = resolve_path_e "#{path}/AndroidManifest.xml"
    dir_jars = resolve_path_e "#{path}/jars"
    @file_jar = resolve_path_e "#{dir_jars}/classes.jar"
    @dependencies = resolve_dependencies dir_jars
    @dir_res = resolve_path_e "#{path}/res"

  end

  def resolve_dependencies(dir_jars)
    dependencies = []
    Dir["#{dir_jars}/libs/*.jar"].each { |file_jar|
      dependencies.push File.expand_path(file_jar)
    }
    return dependencies
  end

  def to_s
    return "#{name}:#{version} #{path}"
  end
end

class AndroidAARBuildResult
  attr_reader :artifacts, :file_aar

  def initialize(file_aar)
    @file_aar = resolve_path file_aar
    @artifacts = {}

    dir_dependencies = resolve_path "#{File.dirname(file_aar)}/../../intermediates/exploded-aar"
    Dir.chdir dir_dependencies do
      Dir['*'].each do |dir_package|
        Dir.chdir dir_package do
          Dir['*'].each do |dir_artifact|
            artifact = extract_artifact(dir_artifact)
            @artifacts[artifact.name] = artifact
          end
        end
      end
    end

  end

  def extract_artifact(dir_artifact)

    Dir.chdir dir_artifact do
      dir_version = Dir['*'].first

      artifact_name = File.basename dir_artifact
      artifact_version = File.basename dir_version

      return AndroidArtifact.new(File.expand_path(dir_version), artifact_name, artifact_version)

    end
  end
end