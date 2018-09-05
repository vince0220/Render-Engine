package Engine.Resources.IO.Parsers;

public interface ITypeParser<T,U> {
    boolean CanParse(U line);
    T Parse(U Data);
}
