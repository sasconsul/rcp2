import { BaseEntity } from './../../shared';

export class Words implements BaseEntity {
    constructor(
        public id?: number,
        public word?: string,
        public count?: number,
        public page?: number,
        public pages?: BaseEntity[],
    ) {
    }
}
