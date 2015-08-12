using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace UnityEditor.XCodeEditor.LunarConsole
{
	public class PBXBuildPhase : PBXObject
	{
		protected const string FILES_KEY = "files";
		
		public PBXBuildPhase() :base()
		{
		}
		
		public PBXBuildPhase( string guid, PBXDictionary dictionary ) : base ( guid, dictionary )
		{
		}
		
		public bool AddBuildFile( PBXBuildFile file )
		{
			if( !ContainsKey( FILES_KEY ) ){
				this.Add( FILES_KEY, new PBXList() );
			}
			((PBXList)_data[ FILES_KEY ]).Add( file.guid );
			
			return true;
		}
		
		public void RemoveBuildFile( string id )
		{
			if( !ContainsKey( FILES_KEY ) ) {
				this.Add( FILES_KEY, new PBXList() );
				return;
			}
			
			((PBXList)_data[ FILES_KEY ]).Remove( id );
		}
		
		public bool HasBuildFile( string id )
		{
			if( !ContainsKey( FILES_KEY ) ) {
				this.Add( FILES_KEY, new PBXList() );
				return false;
			}
			
			if( !IsGuid( id ) )
				return false;
			
			return ((PBXList)_data[ FILES_KEY ]).Contains( id );
		}
	}
	
	public class PBXFrameworksBuildPhase : PBXBuildPhase
	{
		public PBXFrameworksBuildPhase( string guid, PBXDictionary dictionary ) : base ( guid, dictionary )
		{
		}
	}

	public class PBXResourcesBuildPhase : PBXBuildPhase
	{
		public PBXResourcesBuildPhase( string guid, PBXDictionary dictionary ) : base ( guid, dictionary )
		{
		}
	}

	public class PBXShellScriptBuildPhase : PBXBuildPhase
	{
		public PBXShellScriptBuildPhase( string guid, PBXDictionary dictionary ) : base ( guid, dictionary )
		{
		}
	}

	public class PBXSourcesBuildPhase : PBXBuildPhase
	{
		public PBXSourcesBuildPhase( string guid, PBXDictionary dictionary ) : base ( guid, dictionary )
		{
		}
	}

	public class PBXCopyFilesBuildPhase : PBXBuildPhase
	{
		public PBXCopyFilesBuildPhase( string guid, PBXDictionary dictionary ) : base ( guid, dictionary )
		{
		}
	}
}
