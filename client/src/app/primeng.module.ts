import { NgModule } from "@angular/core";

import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { PaginatorModule } from 'primeng/paginator';
import { CardModule } from 'primeng/card';
import { FieldsetModule } from 'primeng/fieldset';
import { AccordionModule } from 'primeng/accordion';
import { PanelModule } from 'primeng/panel';
import { ProgressBarModule } from 'primeng/progressbar';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { MegaMenuModule } from 'primeng/megamenu';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { FileUploadModule } from 'primeng/fileupload';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DynamicDialogModule } from 'primeng/dynamicdialog';

const primengModules: any[] = [
    MenubarModule, ButtonModule, PaginatorModule, CardModule, FieldsetModule,
    AccordionModule, PanelModule, ProgressBarModule, ToastModule, DialogModule,
    DividerModule, MegaMenuModule, ConfirmDialogModule, InputTextModule, PasswordModule,
    FileUploadModule, ScrollPanelModule, InputTextareaModule, DynamicDialogModule
]

@NgModule({
  imports: primengModules,
  exports: primengModules
})
export class PrimeNgModule { }