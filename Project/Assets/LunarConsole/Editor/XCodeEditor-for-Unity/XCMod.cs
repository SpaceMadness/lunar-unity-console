using UnityEngine;
using System.Collections;
using System.IO;
using Json = UnityEditor.XCodeEditor.LunarConsole.XMiniJSON;

namespace UnityEditor.XCodeEditor.LunarConsole 
{
	public class XCMod 
	{
		private Hashtable _datastore;
		private ArrayList _libs;
		
		public string name { get; private set; }
		public string path { get; private set; }
		
		public string group {
			get {
				return (string)_datastore["group"];
			}
		}
		
		public ArrayList patches {
			get {
				return (ArrayList)_datastore["patches"];
			}
		}
		
		public ArrayList libs {
			get {
				if( _libs == null ) {
					ArrayList libs = (ArrayList)_datastore["libs"];
					if (libs != null) {
						_libs = new ArrayList( libs.Count );
						foreach( string fileRef in libs ) {
							_libs.Add( new XCModFile( fileRef ) );
						}
					} else {
						_libs = new ArrayList( 0 );
					}
				}
				return _libs;
			}
		}
		
		public ArrayList frameworks {
			get {
				return (ArrayList)_datastore["frameworks"];
			}
		}
		
		public ArrayList headerpaths {
			get {
				return (ArrayList)_datastore["headerpaths"];
			}
		}

		public Hashtable buildSettings {
			get {
				return (Hashtable)_datastore["buildSettings"];
			}
		}
		
		public ArrayList files {
			get {
				return (ArrayList)_datastore["files"];
			}
		}
		
		public ArrayList folders {
			get {
				return (ArrayList)_datastore["folders"];
			}
		}
		
		public ArrayList excludes {
			get {
				return (ArrayList)_datastore["excludes"];
			}
		}
		
		public XCMod( string filename )
		{	
			FileInfo projectFileInfo = new FileInfo( filename );
			if( !projectFileInfo.Exists ) {
				Debug.LogWarning( "File does not exist." );
			}
			
			name = System.IO.Path.GetFileNameWithoutExtension( filename );
            path = System.IO.Path.GetDirectoryName( filename );
			
			string contents = projectFileInfo.OpenText().ReadToEnd();
			_datastore = (Hashtable)XMiniJSON.jsonDecode( contents );
		}
	}
	
	public class XCModFile
	{
		public string filePath { get; private set; }
		public bool isWeak { get; private set; }
		
		public XCModFile( string inputString )
		{
			isWeak = false;
			
			if( inputString.Contains( ":" ) ) {
				string[] parts = inputString.Split( ':' );
				filePath = parts[0];
				isWeak = ( parts[1].CompareTo( "weak" ) == 0 );	
			}
			else {
				filePath = inputString;
			}
		}
	}
}