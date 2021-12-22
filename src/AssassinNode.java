
/**
 * The AssassinNode class is used to store the information for one
 * player in the game of assassin.
 */
public class AssassinNode {
    /**
     * This person's name.
     */
    private String name;

    /**
     * The name of who killed this person,
     * <code>null</code> if still alive.
     */
    private String killer;

    /**
     * The next node in the list.
     */
    public AssassinNode next;

    /**
     * Constructs a node with the given name and a <code>null</code> link.
     *
     * @param name The name
     */
    public AssassinNode(String name) {
        this(name, null);
    }

    /**
     * Constructs a node with the given name and link
     *
     * @param name The name of the player.
     * @param next A link to the next player node.
     */
    public AssassinNode(String name, AssassinNode next) {
        this.name = name;
        this.killer = null;
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKiller() {
        return killer;
    }

    public void setKiller(String killer) {
        this.killer = killer;
    }

    public AssassinNode getNext() {
        return next;
    }

    public void next(AssassinNode next) {
        this.next = next;
    }
}