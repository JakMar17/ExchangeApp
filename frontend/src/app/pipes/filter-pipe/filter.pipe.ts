import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter',
})
export class FilterPipe implements PipeTransform {
  /**
   * filters array using filterFunction and filteredBy string
   * @param arrayToBeFiltered array that needs filtering
   * @param filterFunction function takes element and string to be filtered by, returns boolean
   * @param filterBy string to be filtered by
   */
  transform(
    arrayToBeFiltered: any[],
    filterFunction: any,
    filterBy: string
  ): any[] {
    return arrayToBeFiltered.filter((e) => filterFunction(e, filterBy));
  }
}
