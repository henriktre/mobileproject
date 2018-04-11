## when starting a new feature:
1:
```
git checkout master
```
2:
```
git pull
```
3:
```
git checkout -b featurename
```
4:  
Start working on feature  

5:  
when done with feature do
```
git pull origin master
```
6:  
fix merge conflicts and push to feature branch:
```
git add .
git commit -m "message"
git push origin featurename
```
6:  
go to github.com and do a pull request to master from the feature branch
