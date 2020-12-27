import matplotlib.pyplot as plt

file_object = open("tempResult.txt", "r")
value = file_object.readlines()
n = int(value[0])
# Chapter 4 Graph
floats_list1 = []
floats_list2 = []
floats_list3 = []
floats_list4 = []
multiplierList = [10,25,50,75,100]
for i in range(0, n):
    floats_list1.append(float(value[i + 1]))
    floats_list2.append(float(value[i + 1 + n]))
    floats_list3.append(float(value[i + 1 + n + n]))
    floats_list4.append(float(value[i + 1 + n + n + n]))
plt.plot(multiplierList, floats_list1, label="HA")
plt.plot(multiplierList, floats_list2, label="MSNTM")
plt.xlabel('Service')
plt.ylabel('Average Verification time')
plt.legend()
plt.savefig("1MSNTM.jpg")
plt.cla()
plt.clf()
# Chapter 5 Graph
plt.plot(multiplierList, floats_list1, label="HA")
plt.plot(multiplierList, floats_list3, label="MTNTM")
plt.xlabel('Service')
plt.ylabel('Average Verification time')
plt.legend()
plt.savefig("2MTNTM.jpg")
plt.cla()
plt.clf()
# Chapter 6 Graph
plt.plot(multiplierList, floats_list1, label="HA")
plt.plot(multiplierList, floats_list4, label="SNTMM")
plt.xlabel('Service')
plt.ylabel('Average Verification time')
plt.legend()
plt.savefig("3SNTMM.jpg")
plt.cla()
plt.clf()
# Chapter Comparison Graph
plt.plot(multiplierList, floats_list1, label="HA")
plt.plot(multiplierList, floats_list2, label="MSNTM")
plt.plot(multiplierList, floats_list3, label="MTNTM")
plt.plot(multiplierList, floats_list4, label="SNTMM")
plt.xlabel('Service')
plt.ylabel('Average Verification time')
plt.legend()
plt.savefig("4CompareAllWithHA.jpg")
plt.cla()
plt.clf()
# Result Comparison Graph
plt.plot(multiplierList, floats_list2, label="MSNTM")
plt.plot(multiplierList, floats_list3, label="MTNTM")
plt.plot(multiplierList, floats_list4, label="SNTMM")
plt.xlabel('Service')
plt.ylabel('Average Verification time')
plt.legend()
plt.savefig("5CompareAll.jpg")