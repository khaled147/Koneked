dit = 1000
dah = dit*3

def cha_to_vib(x):
    vib1 = 0
    vib2 = 0
    vib3 = 0
    
    if x == ' ':
        vib3 = dit
        vib3 = dit
    elif x == 'e' or x == 'E':
        vib1 = dit
        vib3 = dit
    elif x == 'i' or x == 'I':
        vib2 = dit
        vib3 = dit
    elif x == 't' or x == 'T':
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'a' or x == 'A':
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'n' or x == 'N':
        vib1 = dah
        vib3 = dit
    elif x == 's' or x == 'S':
        vib2 = dah
        vib3 = dit
    elif x == 'd' or x == 'D':
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == 'h' or x == 'H':
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == 'm' or x == 'M':
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == 'r' or x == 'R':
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == 'u' or x == 'U':
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'b' or x == 'B':
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'f' or x == 'F':
        vib1 = dah
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'g' or x == 'G':
        vib2 = dah
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'k' or x == 'K':
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'l' or x == 'L':
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'v' or x == 'V':
        vib1 = dah
        vib1 = dah
        vib3 = dit
    elif x == 'w' or x == 'W':
        vib2 = dah
        vib2 = dah
        vib3 = dit
    elif x == '5':
        vib1 = dit
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == 'c' or x == 'C':
        vib2 = dit
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == 'o' or x == 'O':
        vib1 = dit
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == 'p' or x == 'P':
        vib2 = dit
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == 'x' or x == 'X':
        vib1 = dah
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == 'z' or x == 'Z':
        vib2 = dah
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == '4':
        vib1 = dit
        vib1 = dah
        vib1 = dit
        vib1 = dit
        vib3 = dit
    elif x == '6':
        vib2 = dit
        vib2 = dah
        vib2 = dit
        vib2 = dit
        vib3 = dit
    elif x == 'j' or x == 'J':
        vib1 = dit
        vib1 = dit
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == 'q' or x == 'Q':
        vib2 = dit
        vib2 = dit
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == 'y' or x == 'Y':
        vib1 = dit
        vib1 = dit
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == '3':
        vib2 = dit
        vib2 = dit
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == '7':
        vib1 = dah
        vib1 = dah
        vib1 = dit
        vib3 = dit
    elif x == '2':
        vib2 = dah
        vib2 = dah
        vib2 = dit
        vib3 = dit
    elif x == '8':
        vib1 = dah
        vib1 = dit
        vib1 = dah
        vib3 = dit
    elif x == '1':
        vib2 = dah
        vib2 = dit
        vib2 = dah
        vib3 = dit
    elif x == '9':
        vib1 = dit
        vib1 = dah
        vib1 = dah
        vib3 = dit
    elif x == '0':
        vib2 = dit
        vib2 = dah
        vib2 = dah
        vib3 = dit
    print(vib1, vib2, vib3, x)

f = open("test.txt", 'rt')
lines = f.readlines()
for line in lines:
    for cha in line:
        cha_to_vib(cha)