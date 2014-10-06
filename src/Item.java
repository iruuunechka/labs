/**
 * @author Irene Petrova
 */
public class Item<P, R> {
    public final P[] params;
    public final R res;

    public Item(P[] params, R res) {
        this.params = params;
        this.res = res;
    }
}
