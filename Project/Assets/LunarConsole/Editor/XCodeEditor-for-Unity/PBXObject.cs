using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace UnityEditor.XCodeEditor.LunarConsole
{
	public class PBXObject
	{
		protected const string ISA_KEY = "isa";
		//
		protected string _guid;
		protected PBXDictionary _data;
		
		#region Properties
		
		public string guid {
			get {
				if( string.IsNullOrEmpty( _guid ) )
					_guid = GenerateGuid();
				
				return _guid;
			}
		}
		
		public PBXDictionary data {
			get {
				if( _data == null )
					_data = new PBXDictionary();
				
				return _data;
			}
		}
		
		
		#endregion
		#region Constructors
		
		public PBXObject()
		{
			_data = new PBXDictionary();
			_data[ ISA_KEY ] = this.GetType().Name;
			_guid = GenerateGuid();
		}
		
		public PBXObject( string guid ) : this()
		{
			if( IsGuid( guid ) )
				_guid = guid;
		}
		
		public PBXObject( string guid, PBXDictionary dictionary ) : this( guid )
		{
			if( !dictionary.ContainsKey( ISA_KEY ) || ((string)dictionary[ ISA_KEY ]).CompareTo( this.GetType().Name ) != 0 )
				Debug.LogError( "PBXDictionary is not a valid ISA object" );
			
			foreach( KeyValuePair<string, object> item in dictionary ) {
				_data[ item.Key ] = item.Value;
			}
		}
		
		#endregion
		#region Static methods
		
		public static bool IsGuid( string aString )
		{
			return System.Text.RegularExpressions.Regex.IsMatch( aString, @"^[A-F0-9]{24}$" );
		}
		
		public static string GenerateGuid()
		{
			return System.Guid.NewGuid().ToString("N").Substring( 8 ).ToUpper();
		}
		
		
		#endregion
		#region Data manipulation
		
		public void Add( string key, object obj )
		{
			_data.Add( key, obj );
		}
		
		public bool Remove( string key )
		{
			return _data.Remove( key );
		}
		
		public bool ContainsKey( string key )
		{
			return _data.ContainsKey( key );
		}
		
		#endregion
	}
	
	public class PBXNativeTarget : PBXObject
	{
		public PBXNativeTarget() : base() {
		}
		
		public PBXNativeTarget( string guid, PBXDictionary dictionary ) : base( guid, dictionary ) {	
		}
	}

	public class PBXContainerItemProxy : PBXObject
	{
		public PBXContainerItemProxy() : base() {
		}
		
		public PBXContainerItemProxy( string guid, PBXDictionary dictionary ) : base( guid, dictionary ) {	
		}
	}

	public class PBXReferenceProxy : PBXObject
	{
		public PBXReferenceProxy() : base() {
		}
		
		public PBXReferenceProxy( string guid, PBXDictionary dictionary ) : base( guid, dictionary ) {	
		}
	}

	public class PBXVariantGroup : PBXObject
	{
		public PBXVariantGroup() : base() {
		}
		
		public PBXVariantGroup( string guid, PBXDictionary dictionary ) : base( guid, dictionary ) {	
		}
	}
}
