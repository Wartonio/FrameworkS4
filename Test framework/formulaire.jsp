
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
        <form action="Getall" method="post">
            Nom : <input type="text" name="nom"> 
            Num : <input type="text" name="id">
            Date de naissance : <input type="date" name="date">
            <input type="checkbox" name="jours[]" value="lundi">
            <input type="checkbox" name="jours[]" value="mardi">
            <input type="checkbox" name="jours[]" value="jeudi">
            <input type="submit" value="ok">
        </form>
</body>
</html>