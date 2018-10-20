#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'mmkv_plugin'
  s.version          = '0.0.5'
  s.summary          = 'Tencent MMKV Flutter wrapper'
  s.description      = <<-DESC
Tencent MMKV Flutter wrapper
                       DESC
  s.homepage         = 'https://github.com/haoguo/mmkv_plugin'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Bard Guo' => 'bard.guo@gmail.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'MMKV'
  
  s.ios.deployment_target = '8.0'
end

