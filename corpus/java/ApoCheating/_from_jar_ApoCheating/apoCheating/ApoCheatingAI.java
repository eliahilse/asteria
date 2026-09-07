package apoCheating;

import java.util.ArrayList;

public interface ApoCheatingAI
{

	public String getName();
	public String getAuthorName();
	public void think( int[][] aPlayground, ApoCheatingPlayer player, ArrayList<ApoCheatingEntity> entity, ApoCheatingEnemy enemy, ArrayList<ApoCheatingTeacher> teacher );
	public void setPlayer( int player );

}
