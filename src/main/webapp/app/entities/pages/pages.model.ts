import { BaseEntity } from './../../shared';

export class Pages implements BaseEntity {
    constructor(
        public id?: number,
        public url?: string,
    ) {
    }
}
