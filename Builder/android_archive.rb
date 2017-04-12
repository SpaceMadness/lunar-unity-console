require 'rubygems'
require 'zip'

require_relative 'common'

include Common

class AndroidArchive
  def initialize(archive_file)
    @resources = []
    @classes = []

    Zip::File.open(archive_file) do |zip_file|
      zip_file.each do |entry|
        if entry.name.start_with? 'res/'
          @resources.push entry.name
        end
      end
      Dir.mktmpdir do |temp_dir|
        jar_entry = zip_file.glob('classes.jar').first
        dex_entry = zip_file.glob('classes.dex').first

        if jar_entry
          zip_file.extract jar_entry, "#{temp_dir}/classes.jar"
          Zip::File.open "#{temp_dir}/classes.jar" do |classes_zip|
            classes_zip.each do |entry|
              @classes.push entry.name
            end
          end
        elsif dex_entry
          zip_file.extract dex_entry, "#{temp_dir}/classes.dex"
          dexdump = resolve_path Dir["#{File.expand_path('~/Library/Android/sdk/build-tools')}/**/dexdump"].first
          result = exec_shell %("#{dexdump}" "#{temp_dir}/classes.dex"), "Can't list dex classes"
          result.scan(/Class descriptor  : 'L(.*?);'/).each {|match|
            @classes.push "#{match[0]}.class"
          }

        else
          fail_script "Can't list classes: #{archive_file}"
        end
      end
    end
  end

  def list_classes(&filter)
    classes = []
    @classes.each do |name|
      classes.push name if filter.call(name)
    end
    return classes
  end

  def list_resources(&filter)
    resources = []
    @resources.each do |name|
      resources.push name if filter.call(name)
    end
    return resources
  end

end