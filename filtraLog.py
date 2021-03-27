import matplotlib.pyplot as plt
import numpy as np
import re
buffer1 = []
buffer2 = []

for i in open("log.txt","r"):
   
   algo = i.find("tiene ")
  
   if(algo>0):
     aux = i[algo+6:]
     buffer1.append(aux[:aux.index(" elementos")])
     aux2 = aux[(aux.find("tiene ")+6):]
     buffer2.append(aux2[:aux2.index(" eleme")])
     
buffer1 = list(map(lambda x: int(x),buffer1))
buffer2 = list(map(lambda x: int(x),buffer2))


plt.plot(buffer1,'b-', lw="0.5", label="Buffer1")
plt.plot(buffer2,'r-', lw="0.5", label="Buffer2")
plt.ylabel('Cantidad en el buffer')
plt.xlabel('Numero de muestra')
plt.yscale('linear')
plt.legend(loc="upper right")
plt.grid(True, which='both')
plt.axhline(y=0, color='k')
plt.axvline(x=0, color='k')
plt.show()
