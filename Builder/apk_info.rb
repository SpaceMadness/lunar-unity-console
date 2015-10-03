require_relative 'common'

require 'ruby_apk'

include Common

class ApkInfo
  def initialize(apk_file)
    @apk = Android::Apk.new(apk_file)
  end

  def list_classes
    classes = []
    @apk.dex.classes.each do |cls|
      cls.name =~ /L(.*?);/
      classes.push $1
    end
    return classes
  end

  def list_resources(&filter)
    resources = []
    @apk.each_file do |name, data|
      resources.push name if filter.call(name)
    end
    return resources
  end

end