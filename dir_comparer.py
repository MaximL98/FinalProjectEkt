import filecmp
from os import listdir
from os.path import isfile, join


PATH_TO_LOGIC_SERVER = "Server\\FinalProjectEkt_Server\\src\\logic"
PATH_TO_LOGIC_CLIENT = "Client\\finalProjectEkt_Client\\src\\logic"
PATH_TO_COMMON_SERVER = "Server\\FinalProjectEkt_Server\\src\\common"
PATH_TO_COMMON_CLIENT = "Client\\finalProjectEkt_Client\\src\\common"


def compare_directories(dir1, dir2):
    onlyfiles = sorted([f for f in listdir(dir1) if isfile(join(dir1, f))])
    onlyfiles2 = sorted([f for f in listdir(dir2) if isfile(join(dir2, f))])
    if len(onlyfiles) != len(onlyfiles2):
        print("File is missing, showing all files:")
        print(f"Files in {dir1}:")
        for f1 in onlyfiles:
            print(f1)
        print(f"Files in {dir2}:")
        for f2 in onlyfiles2:
            print(f2)
        return False
    flag = True
    for f1, f2 in zip(onlyfiles, onlyfiles2):
        p1, p2 = dir1+"\\"+f1, dir2+"\\"+f2
        if not (filecmp.cmp(p1, p2)):
            print(f"File={p1}\nis different than file={p2}")
            flag = False
    return flag


def compare_logic():
    return compare_directories(PATH_TO_LOGIC_SERVER, PATH_TO_LOGIC_CLIENT)


def compare_common():
    return compare_directories(PATH_TO_COMMON_SERVER, PATH_TO_COMMON_CLIENT)


if __name__ == "__main__":
    compare_common()
    compare_logic()
    input("enter to exit")


