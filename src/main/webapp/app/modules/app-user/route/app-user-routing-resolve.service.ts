/* eslint-disable @typescript-eslint/no-unnecessary-condition */
import { inject } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { ActivatedRouteSnapshot, Router } from "@angular/router";
import { EMPTY, Observable, of } from "rxjs";
import { catchError, map, mergeMap } from "rxjs/operators";
import { IAppUser } from "app/entities/app-user/app-user.model";
import {
  AppUserService,
  EntityResponseType,
} from "app/entities/app-user/service/app-user.service";
import { IUserAppUserDTO } from "app/shared/dto/user-appuser-dto.model";

const appUserResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IAppUser> => {
  const id = route.params.id;
  if (id) {
    return inject(AppUserService)
      .find(id)
      .pipe(
        mergeMap((appUser: HttpResponse<IAppUser>) => {
          if (appUser.body) {
            return of(appUser.body);
          }
          inject(Router).navigate(["404"]);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default appUserResolve;

// const userAppUserDTOResolve = (
//   route: ActivatedRouteSnapshot,
// ): Observable<null | IUserAppUserDTO> => {
//   const id = route.params.id;
//   if (id) {
//     return inject(AppUserService)
//       .findUserAppUserDTO(id)
//       .pipe(
//         map((response: EntityResponseType) => {
//           // Conversion explicite du type
//           const userAppUserDTO = response.body as unknown as IUserAppUserDTO;
//           return userAppUserDTO;
//         }),
//         mergeMap((userAppUserDTO: IUserAppUserDTO) => {
//           if (userAppUserDTO) {
//             return of(userAppUserDTO);
//           }
//           inject(Router).navigate(["404"]);
//           return EMPTY;
//         }),
//         catchError(() => {
//           inject(Router).navigate(["404"]);
//           return EMPTY;
//         }),
//       );
//   }
//   return of(null);
// };

// export default userAppUserDTOResolve;
