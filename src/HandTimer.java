/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;

/**
 *
 * @author Maurice
 */
public class HandTimer implements Runnable{

    private TacticalProcessor tactic;
    private final long startTime = new Date().getTime();
    private boolean stop = false;
    //private LoggerOutput logger = new LoggerOutput("HandTimer");
    
    public HandTimer(TacticalProcessor proc)
    {
        this.tactic = proc;
    }
    
    public void stop()
    {
        this.stop = true;
    }
    
    @Override
    public void run() {
        while(!stop)
        {
            if(new Date().getTime() -30 > startTime+InputParser.timeBank)
            {                
                tactic.setBet(tactic.getTable().getBigBlind());
                //logger.write("Timer abgelaufen!");
                System.out.println(tactic.returnAction());
                System.out.flush();
                this.stop = true;
            }
            else
            {
                Thread.yield();
            }
            
        }
        int timeElapsed = (int)(new Date().getTime() - startTime);
        int timeLeft = InputParser.timeBank - timeElapsed;
        InputParser.timeBank = timeLeft;
    }
    
}
