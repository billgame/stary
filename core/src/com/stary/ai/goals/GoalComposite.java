package com.stary.ai.goals;

import java.util.ArrayList;
import java.util.List;

public class GoalComposite extends Goal {
	List<Goal> subGoals=new ArrayList<Goal>();
	public GoalComposite(){
		this.isAtomGoal=false; 
	}
	@Override
	public void addSubGoal(Goal goal) {
		subGoals.add(0,goal);//加到栈顶
	}
	public Goal getTopGoal(){
		return subGoals.size()>0?subGoals.get(0):null;
	}
	public void removeAllSubGoals(){
		for (int i = 0; i < subGoals.size(); i++) {
			subGoals.get(i).terminate();
//			subGoals.remove(i);
		}
		subGoals.clear();
	}
	public GoalState processSubgoals(){
		while (!subGoals.isEmpty() 
				&& (subGoals.get(0).isCompleted()||subGoals.get(0).isFailed())) {
			subGoals.get(0).terminate();
			subGoals.remove(0);//出栈 删除已完成或失败的sub goal
		}
		if (!subGoals.isEmpty()) {
			GoalState subGoalState=subGoals.get(0).process();//处理栈顶的sub goal
//			System.out.println("[GoalComposite]子目标:"+subGoals.get(0).getClass().getSimpleName()+" "+subGoalState);
		    if (subGoalState == GoalState.completed && subGoals.size() > 1)
		    {
		      return GoalState.active;//
		    }
		    return subGoalState;//sub goal失败
		}else {
			return GoalState.completed;
		}
	}
}
