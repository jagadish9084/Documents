Dim Arg, var1, var2, directory, file_end_with
Set Arg = WScript.Arguments
word_1 = Arg(0)
word_2 = Arg(1)
file_end_with = Arg(2)
Const B_Slash = "\"
Const ForReading = 1
Const ForWriting = 2

if wscript.arguments.count > 3 then
    directory = Arg(3)
else
    directory = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName) & B_Slash
end if

Wscript.Echo "Re-Writing files in directory: " & directory
Wscript.Echo word_1
Wscript.Echo word_2


With CreateObject("Scripting.FileSystemObject")
    For Each File In .GetFolder(directory).Files
        If StrComp(Right(File.Name, Len(file_end_with)), file_end_with, vbTextCompare) = 0 Then
            Wscript.Echo "Updating File " & File.Name
            Set objFSO = CreateObject("Scripting.FileSystemObject")
            Set objFile = objFSO.OpenTextFile(File.Name, ForReading)

            strText = objFile.ReadAll
            objFile.Close
            strNewText = Replace(strText, word_1, word_2)

            Set objFile = objFSO.OpenTextFile(File.Name, ForWriting)
            objFile.WriteLine strNewText
            objFile.Close
        End If
    Next
End With
