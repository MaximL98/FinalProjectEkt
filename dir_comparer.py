import difflib
import filecmp
from os import listdir
from os.path import isfile, join

from numpy import diff

PATH_TO_LOGIC_SERVER = "Server\\FinalProjectEkt_Server\\src\\logic"
PATH_TO_LOGIC_CLIENT = "Client\\finalProjectEkt_Client\\src\\logic"
PATH_TO_COMMON_SERVER = "Server\\FinalProjectEkt_Server\\src\\common"
PATH_TO_COMMON_CLIENT = "Client\\finalProjectEkt_Client\\src\\common"


def clean_file(string):
    """Remoes all comments from the file (yes this is a cruel implementation)"""
    res = ""
    new_line_flag = False
    star_slash_flag = False
    for i, _ in enumerate(string):
        if string[i] == '\n':
            new_line_flag = False
        if i < len(string) - 1:
            if string[i]=='/' and string[i + 1] =='/':
                new_line_flag = True
            if string[i]=='/' and string[i + 1] =='*':
                star_slash_flag = True
            if not new_line_flag and not star_slash_flag:
                if len(res) and res[-1]=='/':
                    res = res[:-1]
                res += string[i]
            if string[i]=='*' and string[i + 1] =='/':
                star_slash_flag = False

        else:
            res += string[i]

    return res


def check_uncommented_changes(path_to_file1, path_to_file2):
    """Compare files when all comments and all spaces are removed"""
    with open(path_to_file1, "r") as f1:
        file1 = f1.read()

    with open(path_to_file2) as f2:
        file2 = f2.read()

    file1=clean_file(file1).strip().replace(" ","").lstrip().rstrip()
    file2=clean_file(file2).strip().replace(" ","").lstrip().rstrip()
    while len(file1) and file1[0]==' ':
        file1=file1[1:]
    while len(file2) and file2[0] == ' ':
        file2 = file2[1:]
    # print(file1, file2)
    # print(file1==file2)
    if file1==file2:
        return True
    return list(difflib.Differ().compare(file1.splitlines(True), file2.splitlines(True)))


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
            checkers = check_uncommented_changes(p1, p2)
            # no, it can't
            if checkers == True:
                print(f"Files={p1},{p2}\nAre only different in the comments")
            else: 
                print(f"File={p1}\nis different than file={p2} (en [Deutsche] substance):")
                #print(checkers)
            flag = False
    return flag


def compare_logic():
    return compare_directories(PATH_TO_LOGIC_SERVER, PATH_TO_LOGIC_CLIENT)


def compare_common():
    return compare_directories(PATH_TO_COMMON_SERVER, PATH_TO_COMMON_CLIENT)


if __name__ == "__main__":
    print("Checking files in /common (server and client)")
    compare_common()
    print("Checking files in /logic (server and client)")
    compare_logic()
    input("enter to exit")


