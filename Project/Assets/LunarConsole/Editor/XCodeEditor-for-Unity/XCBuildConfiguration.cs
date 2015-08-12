using UnityEngine;
using System.Collections;

namespace UnityEditor.XCodeEditor.LunarConsole
{
	public class XCBuildConfiguration : PBXObject
	{
		protected const string BUILDSETTINGS_KEY = "buildSettings";
		protected const string HEADER_SEARCH_PATHS_KEY = "HEADER_SEARCH_PATHS";
		protected const string LIBRARY_SEARCH_PATHS_KEY = "LIBRARY_SEARCH_PATHS";
		protected const string FRAMEWORK_SEARCH_PATHS_KEY = "FRAMEWORK_SEARCH_PATHS";
		protected const string OTHER_C_FLAGS_KEY = "OTHER_CFLAGS";
		protected const string OTHER_LD_FLAGS_KEY = "OTHER_LDFLAGS";
		protected const string GCC_ENABLE_CPP_EXCEPTIONS_KEY = "GCC_ENABLE_CPP_EXCEPTIONS";
		protected const string GCC_ENABLE_OBJC_EXCEPTIONS_KEY = "GCC_ENABLE_OBJC_EXCEPTIONS";

		public XCBuildConfiguration( string guid, PBXDictionary dictionary ) : base( guid, dictionary )
		{
			
		}
		
		public PBXDictionary buildSettings {
			get {
				if( ContainsKey( BUILDSETTINGS_KEY ) )
					return (PBXDictionary)_data[BUILDSETTINGS_KEY];
			
				return null;
			}
		}
		
		protected bool AddSearchPaths( string path, string key, bool recursive = true )
		{
			PBXList paths = new PBXList();
			paths.Add( path );
			return AddSearchPaths( paths, key, recursive );
		}
		
		protected bool AddSearchPaths( PBXList paths, string key, bool recursive = true )
		{	
			bool modified = false;
			
			if( !ContainsKey( BUILDSETTINGS_KEY ) )
				this.Add( BUILDSETTINGS_KEY, new PBXDictionary() );
			
			foreach( string path in paths ) {
				string currentPath = path;
				if( recursive && !path.EndsWith( "/**" ) )
					currentPath += "/**";
				
				if( !((PBXDictionary)_data[BUILDSETTINGS_KEY]).ContainsKey( key ) ) {
					((PBXDictionary)_data[BUILDSETTINGS_KEY]).Add( key, new PBXList() );
				}
				else if( ((PBXDictionary)_data[BUILDSETTINGS_KEY])[key] is string ) {
					PBXList list = new PBXList();
					list.Add( ((PBXDictionary)_data[BUILDSETTINGS_KEY])[key] );
					((PBXDictionary)_data[BUILDSETTINGS_KEY])[key] = list;
				}
				
				currentPath = "\\\"" + currentPath + "\\\"";
				
				if( !((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[key]).Contains( currentPath ) ) {
					((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[key]).Add( currentPath );
					modified = true;
				}
			}
		
			return modified;
		}
		
		public bool AddHeaderSearchPaths( PBXList paths, bool recursive = true )
		{
			return this.AddSearchPaths( paths, HEADER_SEARCH_PATHS_KEY, recursive );
		}
		
		public bool AddLibrarySearchPaths( PBXList paths, bool recursive = true )
		{
			return this.AddSearchPaths( paths, LIBRARY_SEARCH_PATHS_KEY, recursive );
		}

		public bool AddFrameworkSearchPaths(PBXList paths, bool recursive = true)
		{
			return this.AddSearchPaths(paths, FRAMEWORK_SEARCH_PATHS_KEY, recursive);
		}
		
		public bool AddOtherCFlags( string flag )
		{
			PBXList flags = new PBXList();
			flags.Add( flag );
			return AddOtherCFlags( flags );
		}
		
		public bool AddOtherCFlags( PBXList flags )
		{
			bool modified = false;
			
			if( !ContainsKey( BUILDSETTINGS_KEY ) )
				this.Add( BUILDSETTINGS_KEY, new PBXDictionary() );
			
			foreach( string flag in flags ) {
				
				if( !((PBXDictionary)_data[BUILDSETTINGS_KEY]).ContainsKey( OTHER_C_FLAGS_KEY ) ) {
					((PBXDictionary)_data[BUILDSETTINGS_KEY]).Add( OTHER_C_FLAGS_KEY, new PBXList() );
				}
				else if ( ((PBXDictionary)_data[BUILDSETTINGS_KEY])[ OTHER_C_FLAGS_KEY ] is string ) {
					string tempString = (string)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_C_FLAGS_KEY];
					((PBXDictionary)_data[BUILDSETTINGS_KEY])[ OTHER_C_FLAGS_KEY ] = new PBXList();
					((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_C_FLAGS_KEY]).Add( tempString );
				}
				
				if( !((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_C_FLAGS_KEY]).Contains( flag ) ) {
					((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_C_FLAGS_KEY]).Add( flag );
					modified = true;
				}
			}
			
			return modified;
		}

		public bool AddOtherLDFlags( string flag )
		{
			PBXList flags = new PBXList();
			flags.Add( flag );
			return AddOtherLDFlags( flags );
		}

		public bool AddOtherLDFlags( PBXList flags )
		{
			bool modified = false;
			
			if( !ContainsKey( BUILDSETTINGS_KEY ) )
				this.Add( BUILDSETTINGS_KEY, new PBXDictionary() );
			
			foreach( string flag in flags ) {
				
				if( !((PBXDictionary)_data[BUILDSETTINGS_KEY]).ContainsKey( OTHER_LD_FLAGS_KEY ) ) {
					((PBXDictionary)_data[BUILDSETTINGS_KEY]).Add( OTHER_LD_FLAGS_KEY, new PBXList() );
				}
				else if ( ((PBXDictionary)_data[BUILDSETTINGS_KEY])[ OTHER_LD_FLAGS_KEY ] is string ) {
					string tempString = (string)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_LD_FLAGS_KEY];
					((PBXDictionary)_data[BUILDSETTINGS_KEY])[ OTHER_LD_FLAGS_KEY ] = new PBXList();
					((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_LD_FLAGS_KEY]).Add( tempString );
				}
				
				if( !((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_LD_FLAGS_KEY]).Contains( flag ) ) {
					((PBXList)((PBXDictionary)_data[BUILDSETTINGS_KEY])[OTHER_LD_FLAGS_KEY]).Add( flag );
					modified = true;
				}
			}
			
			return modified;
		}

		public bool GccEnableCppExceptions (string value)
		{
			if (!ContainsKey (BUILDSETTINGS_KEY))
				this.Add (BUILDSETTINGS_KEY, new PBXDictionary ());

			((PBXDictionary)_data [BUILDSETTINGS_KEY])[GCC_ENABLE_CPP_EXCEPTIONS_KEY] = value;
			return true;
		}

		public bool GccEnableObjCExceptions (string value)
		{
			if (!ContainsKey (BUILDSETTINGS_KEY))
				this.Add (BUILDSETTINGS_KEY, new PBXDictionary ());

			((PBXDictionary)_data [BUILDSETTINGS_KEY])[GCC_ENABLE_OBJC_EXCEPTIONS_KEY] = value;
			return true;
		}
	}
}