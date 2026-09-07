package tutorVolley;

/**
 * TutorBlobbyAI ist die "Künstliche Intelligenz" (AI) des Spielers und steuert diesen.
 * Dieses Interface muß implementiert werden, damit der Spieler gesteuert werden kann.
 * @author Dirk Aporius
 */
public interface TutorVolleyAI
{
	/**
	 * Wird pro Zyklus einmal ausgeführt. Wird TRUE zurückgegeben, versucht der Spieler 
	 * zu springen. 
	 * 
	 */
	public boolean think( TutorVolleyBall ball, TutorVolleyPlayer player );
	
	/**
	 * übergibt den Spielernamen
	 * @return Spielername
	 */
	public String getName();
	
	/**
	 * übergibt den Namen des Autors
	 * @return Autorenname
	 */
	public String getAuthorName();
}