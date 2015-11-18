
public class MovementAnimation {
	private Action action;
	private Animation idleAnim;
	private Animation runAnim;
	private Animation jumpAnim;
	private Animation landAnim;
	private Animation knockbackAnim;
	
	public enum Action {
		IDLE,
		RUN,
		JUMP,
		LAND,
		KNOCKED_BACK;
	}
}
