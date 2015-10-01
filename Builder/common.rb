require 'pathname'
require 'colorize'

module Common

  def print_header(message)
    puts message.colorize(:color => :blue, :background => :light_black)
  end

  # Fails build process
  # Params:
  # +message+:: error message
  def fail_script(message)
    raise "Build failed! #{message}"
  end

  # Fails build process if condition is false
  # Params:
  # +condition+:: bool value to check
  # +message+:: error message
  def fail_script_unless(condition, message)
    fail_script message unless condition
  end

  # Fails build process if file or directory does not exist
  # Params:
  # +path+:: file path to check
  def fail_script_unless_file_exists(path)
    fail_script_unless path != nil && (File.directory?(path) || File.exists?(path)), "File doesn't exist: '#{path}'"
  end

  # Returns path if file or directory exists or fails build if does not
  # Params:
  # +path+:: file path to check
  def resolve_path(path)
    fail_script_unless_file_exists path
    return path
  end

  def resolve_path_e(path)
    return File.expand_path resolve_path(path)
  end

  ############################################################

  def not_nil(value)
    fail_script_unless value != nil, 'Value is nil'
    return value
  end

  ############################################################

  def read_lines(file)
    return File.readlines(file).each {|l| l.chomp!}
  end

  ############################################################

  def exec_shell(command, error_message, options = {})
    puts "Running command: #{command}" unless options[:silent] == true
    result = `#{command}`
    if options[:dont_fail_on_error] == true
      puts error_message unless $?.success?
    else
      fail_script_unless($?.success?, "#{error_message}\nShell failed: #{command}\n#{result}")
    end

    return result
  end

  ############################################################

  def make_relative_path(first, second)
    first_path = Pathname.new first
    second_path = Pathname.new second

    return first_path.relative_path_from(second_path).to_s
  end

  ############################################################

  def list_files(path, &filter)
    result = []

    Dir["#{path}/**/*.*"].each do |file|
      accept = filter.call(file)
      if accept
        result << file
      end
    end
    return result
  end

end