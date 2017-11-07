
import org.rdfhdt.hdt.triples.TripleString;

import java.io.IOException;

public class TripleStringHelper {
    private CharSequence subject;
    private CharSequence predicate;
    private CharSequence object;

    public TripleStringHelper(TripleString ts) {
        this.subject = ts.getSubject();
        this.predicate = ts.getPredicate();
        this.object = ts.getObject();
    }

    public CharSequence asNtriple() throws IOException {
        StringBuilder str = new StringBuilder();
        this.dumpNtriple(str);
        return str;
    }

    public final void dumpNtriple(Appendable out) throws IOException {
        char s0 = this.subject.charAt(0);
        if (s0 != '_' && s0 != '<') {
            out.append('<').append(this.subject).append('>');
        } else {
            out.append(this.subject);
        }

        char p0 = this.predicate.charAt(0);
        if (p0 == '<') {
            out.append(' ').append(this.predicate).append(' ');
        } else {
            out.append(" <").append(this.predicate).append("> ");
        }

        char o0 = this.object.charAt(0);
        if (o0 == '"') {
            escapeString(this.object.toString(), out);
            out.append(" .\n");
        } else if (o0 != '_' && o0 != '<') {
            out.append('<').append(this.object).append("> .\n");
        } else {
            out.append(this.object).append(" .\n");
        }

    }

    public static String escapeString(String label) {
        try {
            StringBuilder sb = new StringBuilder(2 * label.length());
            escapeString(label, sb);
            return sb.toString();
        } catch (IOException var2) {
            throw new AssertionError();
        }
    }

    public static void escapeString(String label, Appendable appendable) throws IOException {
        int first = 0;
        int last = label.length();
        int i;
        char c;
        if (last > 1 && label.charAt(0) == '<' && label.charAt(last - 1) == '>') {
            ++first;
            --last;
        } else if (label.charAt(0) == '"') {
            first = 1;
            appendable.append('"');

            for(i = last - 1; i > 0; --i) {
                c = label.charAt(i);
                if (c == '"') {
                    last = i;
                    break;
                }

                char prev = label.charAt(i - 1);
                if (c == '@' && prev == '"') {
                    last = i - 1;
                    break;
                }

                if (c == '^' && prev == '^') {
                    last = i - 2;
                    break;
                }
            }
        }

        for(i = first; i < last; ++i) {
            c = label.charAt(i);
            if (c == '\\') {
                appendable.append("\\\\");
            } else if (c == '"') {
                appendable.append("\\\"");
            } else if (c == '\n') {
                appendable.append("\\n");
            } else if (c == '\r') {
                appendable.append("\\r");
            } else if (c == '\t') {
                appendable.append("\\t");
            } else if (c >= 0 && c <= '\b' || c == 11 || c == '\f' || c >= 14 && c <= 31 || c >= 127 && c <= '\uffff') {
                appendable.append("\\u");
                appendable.append(toHexString(c, 4));
            } else if (c >= 65536 && c <= 1114111) {
                appendable.append("\\U");
                appendable.append(toHexString(c, 8));
            } else {
                appendable.append(c);
            }
        }

        appendable.append(label.subSequence(last, label.length()));
    }

    public static String unescapeString(String s) {
        int backSlashIdx = s.indexOf(92);
        if (backSlashIdx == -1) {
            return s;
        } else {
            int startIdx = 0;
            int sLength = s.length();

            StringBuilder sb;
            for(sb = new StringBuilder(sLength); backSlashIdx != -1; backSlashIdx = s.indexOf(92, startIdx)) {
                sb.append(s.substring(startIdx, backSlashIdx));
                if (backSlashIdx + 1 >= sLength) {
                    throw new IllegalArgumentException("Unescaped backslash in: " + s);
                }

                char c = s.charAt(backSlashIdx + 1);
                if (c == 't') {
                    sb.append('\t');
                    startIdx = backSlashIdx + 2;
                } else if (c == 'r') {
                    sb.append('\r');
                    startIdx = backSlashIdx + 2;
                } else if (c == 'n') {
                    sb.append('\n');
                    startIdx = backSlashIdx + 2;
                } else if (c == '"') {
                    sb.append('"');
                    startIdx = backSlashIdx + 2;
                } else if (c == '\\') {
                    sb.append('\\');
                    startIdx = backSlashIdx + 2;
                } else {
                    String xx;
                    if (c == 'u') {
                        if (backSlashIdx + 5 >= sLength) {
                            throw new IllegalArgumentException("Incomplete Unicode escape sequence in: " + s);
                        }

                        xx = s.substring(backSlashIdx + 2, backSlashIdx + 6);

                        try {
                            c = (char)Integer.parseInt(xx, 16);
                            sb.append(c);
                            startIdx = backSlashIdx + 6;
                        } catch (NumberFormatException var9) {
                            throw new IllegalArgumentException("Illegal Unicode escape sequence '\\u" + xx + "' in: " + s);
                        }
                    } else {
                        if (c != 'U') {
                            throw new IllegalArgumentException("Unescaped backslash in: " + s);
                        }

                        if (backSlashIdx + 9 >= sLength) {
                            throw new IllegalArgumentException("Incomplete Unicode escape sequence in: " + s);
                        }

                        xx = s.substring(backSlashIdx + 2, backSlashIdx + 10);

                        try {
                            c = (char)Integer.parseInt(xx, 16);
                            sb.append(c);
                            startIdx = backSlashIdx + 10;
                        } catch (NumberFormatException var8) {
                            throw new IllegalArgumentException("Illegal Unicode escape sequence '\\U" + xx + "' in: " + s);
                        }
                    }
                }
            }

            sb.append(s.substring(startIdx));
            return sb.toString();
        }
    }

    public static String toHexString(int decimal, int stringLength) {
        StringBuilder sb = new StringBuilder(stringLength);
        String hexVal = Integer.toHexString(decimal).toUpperCase();
        int nofZeros = stringLength - hexVal.length();

        for(int i = 0; i < nofZeros; ++i) {
            sb.append('0');
        }

        sb.append(hexVal);
        return sb.toString();
    }
}
