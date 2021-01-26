import { Component, OnInit } from '@angular/core';
import { SecurityLevel } from './models/security-level';

@Component({
  selector: 'app-class-add',
  templateUrl: './class-add.component.html',
  styleUrls: ['./class-add.component.scss'],
})
export class ClassAddComponent implements OnInit {
  public selectedSecurityLevel: SecurityLevel | null = null;
  public securityLevels: SecurityLevel[] = [
    {
      securityLevelTitle: 'javen',
      securityLevelDescription:
        'Do predmeta lahko dostopa in sodeluje vsak prijavljen uporabnik.',
      securityLevelIcon: 'ri-global-line',
    },
    {
      securityLevelTitle: 'zaščiten z geslom',
      securityLevelDescription:
        'Pred prvim dostopom do predmeta mora uporabnik vpisati prednastavljeno geslo.',
      securityLevelIcon: 'ri-lock-2-line',
    },
    {
      securityLevelTitle: 'omejen z vpisno številko',
      securityLevelDescription:
        'Do predmeta lahko dostopajo samo uporabniki, katerih vpisna številka je omogočena na definiranem seznamu (whitelist).',
      securityLevelIcon: 'ri-file-list-3-line',
    },
    {
      securityLevelTitle: 'omejen z epošto',
      securityLevelDescription:
        'Do predmeta lahko dostopajo samo uporabniki, katerih epošta je omogočena na definiranem seznamu (whitelist).',
      securityLevelIcon: 'ri-at-line',
    },
  ];

  constructor() {}

  ngOnInit(): void {
    this.selectedSecurityLevel = this.securityLevels[0];
  }

  public onSecurityLevelSelected(selected: SecurityLevel) {
    this.selectedSecurityLevel = selected;
  }
}
