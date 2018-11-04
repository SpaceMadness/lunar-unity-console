module Common
  class Platform
    def self.unity_export
      return '/Applications/Unity-Export/Unity.app/Contents/MacOS/Unity' if is_mac_os
      return 'C:\Program Files\Unity-Export\Editor\Unity.exe'
    end

    def self.unity_publish
      return '/Applications/Unity-Publish/Unity.app/Contents/MacOS/Unity' if is_mac_os
      return 'C:\Program Files\Unity-Publish\Editor\Unity.exe'
    end

    private
    def self.is_mac_os
      host_os = RbConfig::CONFIG['host_os']
      case host_os
        when /darwin|mac os/
          return true
        else
          return false
      end
    end
  end
end