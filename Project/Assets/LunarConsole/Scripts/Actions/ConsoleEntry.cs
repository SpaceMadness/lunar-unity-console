namespace LunarConsolePluginInternal
{
    public abstract class ConsoleEntry
    {
        public readonly int Id;

        protected ConsoleEntry(int id)
        {
            Id = id;
        }

        protected bool Equals(ConsoleEntry other)
        {
            return Id == other.Id;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((ConsoleEntry) obj);
        }
    }
}