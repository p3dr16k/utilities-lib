package org.pedrick.adt;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*=======================================================================

*FILE:         BufferFIFO.java
*
*DESCRIPTION:  A simple and lightweight generic and synchronized buffer using a FIFO approach
*REQUIREMENTS: 
*AUTHOR:       Patrick Facco
*VERSION:      1.0
*CREATED:      5-nov-2014
*LICENSE:      GNU/GPLv3
*COPYRIGTH:    Patrick Facco 2014
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*========================================================================
* 
*/

public class BufferFIFO<T>
{
    private final int bufferMaxSize;
    private final LinkedList<T> fifo;
    private int numelem;
    private boolean log;

    /**
     * Costruct a new buffer
     * @param dim Max dimension of the buffer
     */
    public BufferFIFO(int dim)
    {
        bufferMaxSize = dim;
        fifo = new LinkedList<T>();
        numelem = 0;        
    }

    /**
     * Getter for log
     * @return true if log is setted, false otherwise
     */
    public boolean isLog()
    {
        return log;
    }

    /**
     * Setter for log
     * @param log turn on/off the logging
     */
    public void setLog(boolean log)
    {
        this.log = log;
    }    
    
    /**
     * Pop an element from the buffer, if buffer is empty wait until at least 
     * one element is pushed
     * @return T the element popped
     */
    public synchronized T pop()
    {
        try
        {
            while(fifo.isEmpty())
            {
                if(log)
                {
                    Logger.getLogger(BufferFIFO.class.getName()).log(Level.INFO, "FIFO is empty", "");
                }
                wait();
            }
        }
        catch(InterruptedException ex)
        {
            System.err.println("Interrupted Exception in BufferFIFO pop");
        }        
        numelem--;
        T toreturn = fifo.removeLast();
        notifyAll();
        return toreturn;
    }

    /**
     * Push an element into the buffer, if buffer is full wait until at least 
     * one element is popped
     * @param element the element to push
     */
    public synchronized void push(T element)
    {       
       try
       {
            while(isFull())
            {
                if(log)
                {
                    Logger.getLogger(BufferFIFO.class.getName()).log(Level.INFO, "FIFO is full", "");
                }
                wait();
             
            
            }
       }
       catch(InterruptedException ex)
       {
           System.err.println("Interrupted Exception in BufferFIFO push");
       }
       fifo.addFirst(element);
       numelem++;
       notifyAll();
    }

    /**
     * Return a list that rapresent the buffer
     * @return LinkedList a list that rapresent the buffer
     */
    public LinkedList<T> getList()
    {
        return fifo;
    }

    /**Rturn the max size of the buffer
     * @return int the max size of the buffer
     */
    public int getMaxSize()
    {
        return bufferMaxSize;
    }
    
    /**
     * Return the number of elements stored on buffer
     * @return int the number of elements stored on buffer
     */
    public int getSize()
    {
        return numelem;
    }
    
    /**
     * If buffer is empty return true, false otherwise
     * @return boolean if buffer is empty return true, false otherwise
     */
    public boolean isEmpty()
    {
        return numelem == 0;
    }
    
    /**
     * If buffer is full return true, false otherwise
     * @return boolean if buffer is full return true, false otherwise
     */
    public boolean isFull()
    {
        return numelem == bufferMaxSize;
    }

    /**
     * Return the buffer as a String
     * @return String the buffer as a String
     */
    @Override
    public String toString()
    {
        String res = "";
        for(T t: fifo)
        {
            res +=  t;
            res +="\n";
        }        
        return res;
    }    
    
}
