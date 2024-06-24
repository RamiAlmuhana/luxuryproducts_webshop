import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReturnService } from '../../services/return.service';
import { Return } from '../../models/return.model';
import { UserService } from '../../services/user.service';
import { ReturnListModel } from '../../models/returnList.model';

@Component({
  selector: 'app-order-history',
  templateUrl: './admin-retour.component.html',
  imports: [CommonModule, RouterLink, FormsModule, ReactiveFormsModule],
  standalone: true,
  styleUrls: ['./admin-retour.component.scss'],
})
export class AdminRetourComponent implements OnInit {
  returns: Return[];
  returnsOfUser: ReturnListModel[] = [];
  constructor(private returnService: ReturnService) {}

  ngOnInit(): void {
    this.loadReturns();
  }

  loadReturns(): void {
    this.returnService.getReturns().subscribe((returns) => {
      this.returns = returns;
      this.loadUsers();
    });
  }
  loadUsers(): void {
    for (let i = 0; i < this.returns.length; i++) {
      const return1 = this.returns[i];
      const nameFound = this.checkUserEmail(return1);
      if (!nameFound) {
        this.addUserModel(return1);
      }
    }
  }

  checkUserEmail(return1: Return): boolean {
    console.log(return1);
    for (let j = 0; j < this.returnsOfUser.length; j++) {
      const returnListModel = this.returnsOfUser[j];
      if (return1.user.email == returnListModel.user.email) {
        returnListModel.returns.push(return1);
        return true;
      }
    }
    return false;
  }

  addUserModel(return1: Return): void {
    const returnListUser1: ReturnListModel = {
      returns: [return1],
      user: { ...return1.user },
    };
    this.returnsOfUser.push(returnListUser1);
  }

  onAccept(returnID: number) {
    const reason = prompt('Please provide a reason for accepting the return:');
    if (!reason) {
      alert('Accept reason is required');
      return;
    }
    this.updateReturnStatus('Accepted', reason, returnID);
  }

  onDenied(returnID: number) {
    const reason = prompt('Please provide a reason for denying the return:');
    if (!reason) {
      alert('Deny reason is required');
      return;
    }
    this.updateReturnStatus('Denied', reason, returnID);
  }

  updateReturnStatus(status: string, reason: string, returnID: number) {
    const returnData = {
      returnStatus: status,
      adminReason: reason,
    };
    this.returnService.updateReturns(returnData, returnID).subscribe((text) => {
      alert(text);
    });
  }
}
