package v.rabetsky.config;

/** Thread-local хранилище текущей роли. */
public final class RoleContext {
    private static final ThreadLocal<Role> CURRENT = new ThreadLocal<>();

    private RoleContext() {}

    public static void set(Role r)   { CURRENT.set(r); }
    public static Role get()         { return CURRENT.get(); }
    public static void clear()       { CURRENT.remove(); }
}
