import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'search'
})
export class SearchPipe implements PipeTransform {
  transform(userList:any,searchText:any): any {

    let newList:any;
    if(searchText)
    {
      newList=userList.filter(user=>user.name.toLowerCase().startsWith(searchText.toLowerCase()));
    }
    else
    {
      newList=userList;
    }
    return newList;
  }

}
