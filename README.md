#This is a Shipment Manager Application.

Example usage:  
root [QUANTITY]  
split [X,Y] [QUANTITY,QUANTITY,...]  
merge [X,Y X,Y ...]  
exit  
You should use 'root' command to set a root quantity first.  
->command:root 100  
  100    
->command:split 0,0 20,40,40  
        100          
  20    40    40    
->command:merge 1,0 1,1  
        100          
    20        40      40    
    60      
->command:root 50  
        50          
    10        20      20    
    30     
