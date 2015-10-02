package com.stary.ai.goals;

import com.badlogic.ashley.core.Entity;

public class Goal {
	public enum GoalState{
		active,completed,inactive,failed
	}
	public enum GoalType{
		move,attack;
	}
	public GoalType type=null;
	public Entity owner=null;
	public boolean isAtomGoal=false;
	public GoalState state=GoalState.inactive;
	
	public float offsetTime;
	public float duration;
	public float elapsedTime;
//	public Goal(Entity owner){
//		this.owner=owner;
//	}
	public void active(){
	}
	public GoalState process(){
		return GoalState.inactive;
	}
	public void terminate(){
		
	}
//	public boolean handleMessage(Telegram g){
//		return false;
//	}
	public void addSubGoal(Goal goal){
		
	}
	public boolean isActive(){
		return state==GoalState.active;
	}
	public boolean isInActive(){
		return state==GoalState.inactive;
	}
	public boolean isCompleted(){
		return state==GoalState.completed;
	}
	public boolean isFailed(){
		return state==GoalState.failed;
	}

	public void activateIfInactive(){
		if (state==GoalState.inactive) {
			active();
		}
	}
	public void reactivateIfFailed(){
		if (state==GoalState.failed) {
			state=GoalState.inactive;
		}
	}
}
