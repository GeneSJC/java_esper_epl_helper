package com.esperhelper.event.score;

public class ScoreEvent {
    private String type;
    private int score;

    public ScoreEvent(String itemName, int price) {
        this.type = itemName;
        this.score = price;
    }

    public String getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

	@Override
	public String toString() {
		return "ScoreEvent [score=" + score + "]";
	}
    
    
}